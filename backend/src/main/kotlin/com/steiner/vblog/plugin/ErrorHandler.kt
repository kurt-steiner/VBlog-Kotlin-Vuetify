package com.steiner.vblog.plugin

import com.steiner.vblog.util.Response
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.requestvalidation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import kotlinx.io.files.FileNotFoundException

fun Application.configureErrorHandler() {
    val logger = log
    install(StatusPages) {
        exception<BadRequestException> { call, cause ->
            logger.error(cause.stackTraceToString())
            call.respond(HttpStatusCode.BadRequest, Response.Err(cause.message ?: "bad request"))
        }

        exception<NullPointerException> { call, cause ->
            logger.error(cause.stackTraceToString())
            call.respond(HttpStatusCode.InternalServerError, Response.Err(cause.message ?: "null pointer"))
        }

        exception<RequestValidationException> {call, cause ->
            call.respond(HttpStatusCode.BadRequest, Response.Err(cause.reasons.toString()))
        }

        exception<FileNotFoundException> { call, cause ->
            logger.error(cause.stackTraceToString())
            call.respond(HttpStatusCode.InternalServerError, Response.Err(cause.message ?: "file not exist"))
        }

        exception<Exception> { call, cause ->
            logger.error(cause.stackTraceToString())
            call.respond(HttpStatusCode.InternalServerError, Response.Err(cause.message ?: "internal server error"))
        }
    }
}