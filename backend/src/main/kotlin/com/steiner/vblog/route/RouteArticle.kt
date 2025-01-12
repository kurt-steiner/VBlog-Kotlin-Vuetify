package com.steiner.vblog.route

import com.steiner.vblog.ArticleConfigure
import com.steiner.vblog.model.*
import com.steiner.vblog.request.PostArticleRequest
import com.steiner.vblog.request.PutArticleRequest
import com.steiner.vblog.service.ArticleService
import com.steiner.vblog.service.UserService
import com.steiner.vblog.util.Response
import com.steiner.vblog.util.currentUser
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import kotlinx.serialization.json.Json
import org.koin.core.qualifier.named
import org.koin.ktor.ext.inject

fun Application.routeArticle() {
    val articleService: ArticleService by inject<ArticleService>()
    val userService: UserService by inject<UserService>()
    val json: Json by inject<Json>(named("json"))

    routing {
        authenticate("auth-jwt") {
            route("/article") {
                post {
                    val request = call.receive<PostArticleRequest>()
                    val result = articleService.insertOne(request)

                    call.respond(Response.Ok("insert ok", result))
                }

                delete("/{id}") {
                    val id = call.pathParameters.getOrFail<Int>("id")
                    articleService.deleteOne(id)

                    call.respond(Response.Ok("delete ok", Unit))
                }

                put {
                    val request = call.receive<PutArticleRequest>()
                    val result = articleService.updateOne(request)

                    call.respond(Response.Ok("update ok", result))
                }

                get {
                    val user = currentUser(userService)
                    val status = call.queryParameters["status"]

                    if (status != null && status !in arrayOf("0", "1", "2", "3")) {
                        throw BadRequestException("unknown status code")
                    }

                    val tagId = call.queryParameters["tag-id"]?.let {
                        it.toIntOrNull() ?: throw BadRequestException("cannot parse tag id")
                    }

                    val categoryId = call.queryParameters["category-id"]?.let {
                        it.toIntOrNull() ?: throw BadRequestException("cannot parse category id")
                    }

                    val title = call.queryParameters["title"]
                    val page = call.queryParameters["page"]?.toIntOrNull() ?: ArticleConfigure.QUERY_PAGE
                    val size = call.queryParameters["size"]?.toIntOrNull() ?: ArticleConfigure.QUERY_SIZE
                    val reverse = call.queryParameters["reverse"] == "true"
                    val sortBy = when (call.queryParameters["sort-by"]) {
                        "title" -> SortBy.ByTitle
                        "edit-time" -> SortBy.ByEditTime
                        "publish-date" -> SortBy.ByPublishDate
                        else -> SortBy.ByEditTime
                    }

                    val articleQuery = ArticleQuery(
                        authorId = user.id,
                        status = if (status == null) {
                            null
                        } else {
                            json.decodeFromString<ArticleStatus>(status)
                        },

                        title = title,
                        tagId = tagId,
                        sortBy = ArticleSortBy(sortBy = sortBy, reverse = reverse),
                        categoryId = categoryId
                    )

                    val articles = articleService.findAll(query = articleQuery, page = page, size = size)
                    call.respond(Response.Ok("all articles", articles))
                }

                get("/{id}") {
                    val id = call.pathParameters.getOrFail<Int>("id")
                    val article = articleService.findOne(id) ?: throw BadRequestException("no such article with id: $id")

                    val user = currentUser(userService)

                    if (article.author.id == user.id) {
                        call.respond(Response.Ok("this article", article))
                    } else {
                        throw BadRequestException("no such article with id: $id")
                    }
                }
            }
        }

    }

}