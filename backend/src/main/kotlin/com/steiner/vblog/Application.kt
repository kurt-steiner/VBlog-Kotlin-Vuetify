package com.steiner.vblog

import com.steiner.vblog.plugin.*
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.cio.EngineMain.main(args)
}

fun Application.module() {
    configureKoin()
    configureSerialization()
    configureErrorHandler()
    configureValidation()
    configureSecurity()
    configureHTTP()
    configureRouting()
}
