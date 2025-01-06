package com.steiner.vblog.route

import com.steiner.vblog.model.Article
import com.steiner.vblog.model.ArticleQuery
import com.steiner.vblog.request.PostArticleRequest
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
import org.koin.ktor.ext.inject

fun Application.routeArticle() {
    val articleService: ArticleService by inject<ArticleService>()
    val userService: UserService by inject<UserService>()

    routing {
        authenticate("auth-jwt") {
            route("/article") {
                post {
                    val request = call.receive<PostArticleRequest>()
                    val result = articleService.insertOne(request)

                    call.respond(Response.Ok("insert ok", result))
                }

                get {
                    val user = currentUser(userService)
                    val query = call.queryParameters["query"] ?: "author"
                    val queryContent = call.queryParameters["content"]
                    val page = call.queryParameters["page"]?.toIntOrNull() ?: 0
                    val size = call.queryParameters["size"]?.toIntOrNull() ?: 20

                    val articleQuery = when (query) {
                        "author" -> ArticleQuery.Author(user.id)
                        "status" -> {
                            val code = queryContent?.toIntOrNull() ?: throw BadRequestException("query parameter error: content")
                            ArticleQuery.Status(user.id, Article.decodeStatus(code))
                        }

                        "title" -> {
                            if (queryContent == null) {
                                throw BadRequestException("empty content")
                            }

                            ArticleQuery.Title(user.id, queryContent)
                        }

                        else -> throw BadRequestException("unknown query parameter")
                    }

                    val articles = articleService.findAll(query = articleQuery, page = page, size = size)

                    call.respond(Response.Ok("all articles", articles))
                }

                delete("/{id}") {
                    val id = call.pathParameters.getOrFail<Int>("id")
                    articleService.deleteOne(id)

                    call.respond(Response.Ok("delete ok", Unit))
                }
            }
        }

    }

}