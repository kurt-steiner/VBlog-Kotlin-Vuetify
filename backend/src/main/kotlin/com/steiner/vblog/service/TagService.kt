package com.steiner.vblog.service

import com.steiner.vblog.model.Tag
import com.steiner.vblog.request.PostTagRequest
import com.steiner.vblog.request.PutTagRequest
import com.steiner.vblog.table.ArticleTag
import com.steiner.vblog.table.Tags
import com.steiner.vblog.util.dbQuery
import io.ktor.server.plugins.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class TagService(val database: Database) {
    init {
        transaction(database) {
            SchemaUtils.create(Tags)
        }
    }

    suspend fun insertOne(request: PostTagRequest): Tag = dbQuery(database) {
        // check if exist
        val tag = with (Tags) {
            selectAll().where((name eq request.name) and (this.userId eq userId) )
                .firstOrNull()?.let {
                    Tag(
                        id = it[id].value,
                        name = it[name],
                        userId = it[this.userId].value
                    )
                }
        }

        return@dbQuery if (tag == null) {
            val id = with (Tags) {
                insert {
                    it[name] = request.name
                    it[userId] = request.userId
                } get this.id
            }

            findOne(id.value)!!
        } else {
            tag
        }
    }

    suspend fun deleteOne(id: Int) = dbQuery(database) {
        with (ArticleTag) {
            deleteWhere {
                tagId eq id
            }
        }

        with (Tags) {
            deleteWhere {
                this.id eq id
            }
        }
    }

    suspend fun updateOne(request: PutTagRequest): Tag = dbQuery(database) {
        val exist = with (Tags) {
            selectAll().where(id eq request.id)
                .firstOrNull() != null
        }

        if (!exist) {
            throw BadRequestException("no such category with id: ${request.id}")
        }

        with (Tags) {
            update({ id eq request.id }) {
                it[name] = request.name
            }
        }

        return@dbQuery findOne(request.id)!!
    }

    suspend fun findOne(id: Int): Tag? = dbQuery(database) {
        return@dbQuery with (Tags) {
            selectAll().where(this.id eq id )
                .firstOrNull()?.let {
                    Tag(
                        id = it[this.id].value,
                        name = it[name],
                        userId = it[this.userId].value
                    )
                }
        }
    }

    suspend fun findAllOfArticle(articleId: Int): List<Tag> = dbQuery(database) {
        val tagIds = with (ArticleTag) {
            selectAll().where(this.articleId eq articleId)
                .map {
                    it[tagId].value
                }
        }

        return@dbQuery tagIds.map {
            findOne(it)!!
        }
    }

    suspend fun findAllOfAuthor(authorId: Int): List<Tag> = dbQuery(database) {
        with (Tags) {
            selectAll().where(userId eq authorId)
                .map {
                    findOne(it[id].value)!!
                }
        }
    }

    suspend fun clear() = dbQuery(database) {
        Tags.deleteAll()
        ArticleTag.deleteAll()
    }
}