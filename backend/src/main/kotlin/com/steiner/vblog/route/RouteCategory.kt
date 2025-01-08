package com.steiner.vblog.route

import com.steiner.vblog.request.PostCategoryRequest
import com.steiner.vblog.request.PutCategoryRequest
import com.steiner.vblog.service.CategoryService
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

fun Application.routeCategory() {
    val categoryService: CategoryService by inject<CategoryService>()
    val userService: UserService by inject<UserService>()

    routing {
        authenticate("auth-jwt") {
            route("/category") {
                get {
                    val user = currentUser(userService)
                    call.respond(Response.Ok("all categories", categoryService.findAll(user.id)))
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