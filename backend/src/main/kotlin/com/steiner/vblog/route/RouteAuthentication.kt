package com.steiner.vblog.route

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.steiner.vblog.exception.LoginException
import com.steiner.vblog.request.LoginRequest
import com.steiner.vblog.request.RegisterRequest
import com.steiner.vblog.service.UserService
import com.steiner.vblog.util.Response
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.datetime.Clock
import kotlinx.datetime.toJavaInstant
import org.koin.core.qualifier.named
import org.koin.ktor.ext.inject
import kotlin.time.DurationUnit
import kotlin.time.toDuration

fun Application.routeAuthentication() {
    val audience: String by inject<String>(named("jwt.audience"))
    val domain: String by inject<String>(named("jwt.domain"))
    val secret: String by inject<String>(named("jwt.secret"))
    val userService: UserService by inject<UserService>()

    routing {
        post("/login") {
            val request = call.receive<LoginRequest>()
            val success = userService.matchUser(request.name, request.passwordHash)

            if (!success) {
                throw LoginException("no such user of password not correct")
            }

            val currentNow = Clock.System.now()
            val expiredAt = currentNow.plus(2.toDuration(DurationUnit.DAYS)).toJavaInstant()
            val token = JWT.create()
                .withAudience(audience)
                .withIssuer(domain)
                .withClaim("username", request.name)
                .withExpiresAt(expiredAt)
                .sign(Algorithm.HMAC256(secret))

            call.respond(Response.Ok("login success", token))
        }

        post("/register") {
            val request = call.receive<RegisterRequest>()
            userService.register(request)

            call.respond(Response.Ok("register ok", Unit))
        }
    }

}