package com.steiner.vblog.util

import com.steiner.vblog.model.User
import com.steiner.vblog.service.UserService
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.*
import io.ktor.server.routing.*

suspend fun RoutingContext.currentUser(userService: UserService): User {
    val principal = call.principal<JWTPrincipal>() ?: throw BadRequestException("authentication failed")
    val username = principal.payload.getClaim("username").asString()
    return userService.findOne(username) ?: throw BadRequestException("no such user with name: $username")
}