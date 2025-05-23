package com.steiner.vblog.service

import com.steiner.vblog.model.*
import com.steiner.vblog.request.PostArticleRequest
import com.steiner.vblog.request.PutArticleRequest
import com.steiner.vblog.table.ArticleTag
import com.steiner.vblog.table.Articles
import com.steiner.vblog.util.Page
import com.steiner.vblog.util.dbQuery
import io.ktor.server.plugins.*
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.jsoup.Jsoup
import org.jsoup.safety.Safelist
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import kotlin.math.ceil

class ArticleService(val database: Database): KoinComponent {
    init {
        transaction(database) {
            SchemaUtils.create(Articles)
            SchemaUtils.create(ArticleTag)
        }
    }

    val userService: UserService by inject<UserService>()
    val categoryService: CategoryService by inject<CategoryService>()
    val tagService: TagService by inject<TagService>()
    val summaryMaxLength: Int by inject<Int>(named("summary.max-length"))

    suspend fun insertOne(request: PostArticleRequest): ArticleShortcut = dbQuery(database) {
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val id = with (Articles) {
            insert {
                it[title] = request.title
                it[markdownContent] = request.markdownContent
                it[htmlContent] = sanitizeHtml(request.htmlContent)
                it[categoryId] = request.categoryId
                // summary
                it[summary] = generateSummary(request.htmlContent)
                // authorId
                it[authorId] = request.authorId
                // publishDate
                it[publishDate] = now
                // editTime
                it[editTime] = now
                // status
                it[status] = request.status
            } get id
        }

        with (ArticleTag) {
            request.tags?.forEach { tag ->
                insert {
                    it[articleId] = id.value
                    it[tagId] = tag.id
                }
            }
        }

        val article = findOne(id.value)!!
        ArticleShortcut.fromArticle(article)
    }

    suspend fun deleteAllOfAuthor(userId: Int) = dbQuery(database) {
        with (Articles) {
            deleteWhere {
                this.authorId eq userId
            }
        }
    }

    suspend fun deleteOne(id: Int) = dbQuery(database) {
        with (Articles) {
            deleteWhere {
                this.id eq id
            }
        }
    }

    suspend fun updateOne(request: PutArticleRequest): Article = dbQuery(database) {
        val exist = findOne(request.id) != null
        if (!exist) {
            throw BadRequestException("no such article exist with id ${request.id}")
        }

        with (Articles) {
            update({(id eq request.id) and (authorId eq authorId)}) {
                if (request.title != null) {
                    it[title] = request.title
                }

                if (request.markdownContent != null) {
                    it[markdownContent] = request.markdownContent
                }

                if (request.htmlContent != null) {
                    it[htmlContent] = sanitizeHtml(request.htmlContent)
                    it[summary] = generateSummary(request.htmlContent)
                }

                if (request.categoryId != null) {
                    it[categoryId] = request.categoryId
                }

                it[editTime] = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

                if (request.tags != null) {
                    with (ArticleTag) {
                        deleteWhere {
                            this.articleId eq articleId
                        }

                        for (tag in request.tags) {
                            insert { column ->
                                column[this.articleId] = request.id
                                column[tagId] = tag.id
                            }
                        }
                    }
                }

                if (request.status != null) {
                    it[status] = request.status
                }
            }
        }

        findOne(request.id)!!
    }

    suspend fun findOne(articleId: Int): Article? = dbQuery(database) {
        with (Articles) {
            selectAll().where(this.id eq articleId)
                .firstOrNull()?.let {
                    Article(
                        id = it[this.id].value,
                        title = it[title],
                        markdownContent = it[markdownContent],
                        htmlContent = it[htmlContent],
                        summary = it[summary],
                        category = it[categoryId]?.value?.let {
                            categoryService.findOne(it)
                        },

                        author = userService.findOne(it[authorId].value)!!,
                        publishDate = it[publishDate],
                        editTime = it[editTime],
                        status = it[status],
                        tags = tagService.findAllOfArticle(articleId)
                    )
                }
        }
    }

    suspend fun findAll(query: ArticleQuery, page: Int, size: Int): Page<ArticleShortcut> = dbQuery(database) {
        val content = with (Articles) {
            val column = when (query.sortBy?.sortBy) {
                SortBy.ByTitle -> title
                SortBy.ByEditTime -> editTime
                SortBy.ByPublishDate -> publishDate
                null -> title
            }

            val order = when (query.sortBy?.reverse) {
                true -> SortOrder.ASC
                false -> SortOrder.DESC
                null -> SortOrder.ASC
            }

            selectAll().where {
                val flag1 = authorId eq query.authorId
                val flag2 = if (query.status != null) {
                    status eq query.status
                } else {
                    status eq ArticleStatus.Published
                }

                val flag3 = if (query.tagId != null) {
                    val matchedIds = with (ArticleTag) {
                        selectAll().where(tagId eq query.tagId)
                            .map {
                                it[articleId]
                            }
                    }

                    id inList matchedIds
                } else {
                    Op.TRUE
                }

                val flag4 = if (query.categoryId != null) {
                    categoryId eq query.categoryId
                } else {
                    Op.TRUE
                }

                val flag5 = if (query.title != null) {
                    title.lowerCase() like "%${query.title.lowercase()}%"
                } else {
                    Op.TRUE
                }

                flag1 and flag2 and flag3 and flag4 and flag5
            }
                .orderBy(column, order = order)
                .limit(size)
                .offset(page * size.toLong())
                .map {
                    ArticleShortcut(
                        id = it[id].value,
                        title = it[title],
                        summary = it[summary],
                        category = it[categoryId]?.value?.let {
                            categoryService.findOne(it)
                        },
                        author = userService.findOne(it[authorId].value)!!,
                        publishDate = it[publishDate],
                        editTime = it[editTime],
                        status = it[status],
                        tags = tagService.findAllOfArticle(it[id].value)
                    )
                }
        }

        val totalPages = ceil(Articles.selectAll().count() / size.toDouble()).toInt()

        Page(content = content, totalPages = totalPages)
    }

    fun stripHtml(html: String): String {
        return html.replace(Regex("<p\\s+.*?>"), "") // 移除 <p> 标签及其属性
            .replace(Regex("<br\\s*/?>"), "") // 移除 <br> 标签及其变体
            .replace(Regex("<.*?>"), "") // 移除所有其他 HTML 标签
    }

    fun sanitizeHtml(unsafeHtml: String): String {
        // 使用 Jsoup 的 clean 方法与预定义的安全列表 Safelist.basic() 进行净化
        val safelist = Safelist.relaxed()
            .removeTags("script")
            .removeTags("style")

        return Jsoup.clean(unsafeHtml, safelist)
    }

    fun generateSummary(html: String): String {
        val content = stripHtml(html)
        val length = content.length

        return if (length > summaryMaxLength) {
            content.substring(0, summaryMaxLength)
        } else {
            content
        }
    }
}