package com.steiner.vblog.route

import com.steiner.vblog.request.PutUserRequest
import com.steiner.vblog.service.UserService
import com.steiner.vblog.util.Response
import com.steiner.vblog.util.currentUser
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import org.koin.ktor.ext.inject

fun Application.routeUser() {
    val userService: UserService by inject<UserService>()

    routing {
        authenticate("auth-jwt") {
            route("/user") {
                get {
                    val user = currentUser(userService)

                    call.respond(Response.Ok("this user", user))
                }

                put {
                    val request = call.receive<PutUserRequest>()
                    userService.updateOne(request)

                    call.respond(Response.Ok("update ok", Unit))
                }

                delete("/{id}") {
                    val id = call.pathParameters.getOrFail<Int>("id")
                    userService.deleteOne(id)

                    call.respond(Response.Ok("delete ok", Unit))
                }
            }
        }

    }
}