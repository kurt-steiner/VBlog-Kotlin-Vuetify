package com.steiner.vblog.plugin

import com.steiner.vblog.route.*
import io.ktor.server.application.*

fun Application.configureRouting() {
    routeUser()
    routeAuthentication()
    routeArticle()
    routeCategory()
    routeTag()
    routeImage()
}
