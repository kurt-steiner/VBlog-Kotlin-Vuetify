package com.steiner.vblog.plugin

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.serialization.json.Json
import org.koin.core.qualifier.named
import org.koin.ktor.ext.inject

fun Application.configureSerialization() {
    val jsonSerializer: Json by inject<Json>(named("json"))

    install(ContentNegotiation) {
        json(json = jsonSerializer)
    }
}
