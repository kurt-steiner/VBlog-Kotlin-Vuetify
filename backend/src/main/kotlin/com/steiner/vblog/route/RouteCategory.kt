package com.steiner.vblog.route

import com.steiner.vblog.request.PostCategoryRequest
import com.steiner.vblog.request.PutCategoryRequest
import com.steiner.vblog.service.CategoryService
import com.steiner.vblog.util.Response
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import org.koin.ktor.ext.inject

fun Application.routeCategory() {
    val categoryService: CategoryService by inject<CategoryService>()

    routing {
        authenticate("auth-jwt") {
            route("/category") {
                get {
                    call.respond(Response.Ok("all categories", categoryService.findAll()))
                }

                post {
                    val request = call.receive<PostCategoryRequest>()
                    val result = categoryService.insertOne(request)

                    call.respond(Response.Ok("insert ok", result))
                }

                put {
                    val request = call.receive<PutCategoryRequest>()
                    val result = categoryService.updateOne(request)

                    call.respond(Response.Ok("update ok", result))
                }

                delete("/{id}") {
                    val id = call.pathParameters.getOrFail<Int>("id")
                    categoryService.deleteOne(id)

                    call.respond(Response.Ok("delete ok", Unit))
                }
            }
        }
    }

}