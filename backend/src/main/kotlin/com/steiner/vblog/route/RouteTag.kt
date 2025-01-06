package com.steiner.vblog.route

import com.steiner.vblog.request.PostTagRequest
import com.steiner.vblog.request.PutTagRequest
import com.steiner.vblog.service.TagService
import com.steiner.vblog.util.Response
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import org.koin.ktor.ext.inject

fun Application.routeTag() {
    val tagService: TagService by inject<TagService>()

    routing {
        authenticate("auth-jwt") {
            route("/tag") {
                post {
                    val request = call.receive<PostTagRequest>()
                    val result = tagService.insertOne(request)

                    call.respond(Response.Ok("insert ok", result))
                }

                put {
                    val request = call.receive<PutTagRequest>()
                    val result = tagService.updateOne(request)

                    call.respond(Response.Ok("update ok", result))
                }

                delete("/{id}") {
                    val id = call.pathParameters.getOrFail<Int>("id")
                    tagService.deleteOne(id)

                    call.respond(Response.Ok("delete ok", Unit))
                }
            }
        }
    }
}