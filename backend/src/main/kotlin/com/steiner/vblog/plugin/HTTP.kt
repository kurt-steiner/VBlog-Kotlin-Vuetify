package com.steiner.vblog.plugin

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.*

fun Application.configureHTTP() {
    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Patch)
        // allowHeader(HttpHeaders.Authorization)
        allowHeaders { s ->
            true
        }

        anyHost() // @TODO: Don't do this in production if possible. Try to limit it.
    }
}
