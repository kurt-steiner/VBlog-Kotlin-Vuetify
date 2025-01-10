package com.steiner.vblog.route

import com.steiner.vblog.model.Article
import com.steiner.vblog.model.ArticleQuery
import com.steiner.vblog.model.ArticleSortBy
import com.steiner.vblog.model.ArticleStatus
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
                    val query = call.queryParameters["query"] ?: "author"
                    val queryContent = call.queryParameters["content"]
                    val page = call.queryParameters["page"]?.toIntOrNull() ?: 0
                    val size = call.queryParameters["size"]?.toIntOrNull() ?: 20
                    val reverse = call.queryParameters["reverse"] == "true"

                    val sortBy = call.queryParameters["sort-by"]?.let {
                        when (it) {
                            "title" -> ArticleSortBy.ByTitle(reverse)
                            "edit-time" -> ArticleSortBy.ByEditTime(reverse)
                            "publish-date" -> ArticleSortBy.ByPublishDate(reverse)
                            else -> null
                        }
                    } ?: ArticleSortBy.ByEditTime(false)

                    val articleQuery = when (query) {
                        "author" -> ArticleQuery.Author(user.id, sortBy)
                        "status" -> {
                            queryContent?.toIntOrNull() ?: throw BadRequestException("query parameter error: content")
                            ArticleQuery.Status(authorId = user.id, status = json.decodeFromString<ArticleStatus>(queryContent), sortBy = sortBy)
                        }

                        "title" -> {
                            if (queryContent == null) {
                                throw BadRequestException("empty content")
                            }

                            ArticleQuery.Title(user.id, title = queryContent, sortBy = sortBy)
                        }

                        "tag" -> {
                            if (queryContent == null) {
                                throw BadRequestException("empty content")
                            }

                            val tagId = call.queryParameters.getOrFail<Int>("content")
                            ArticleQuery.Tag(user.id, tagId = tagId, sortBy = sortBy)
                        }

                        else -> throw BadRequestException("unknown query parameter")
                    }

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