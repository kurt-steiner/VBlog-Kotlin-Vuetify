package com.steiner.vblog.plugin

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.steiner.vblog.AuthenticationHeader
import com.steiner.vblog.util.Response
import io.ktor.http.*
import io.ktor.http.auth.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import kotlinx.datetime.Clock
import kotlinx.datetime.toJavaInstant
import org.koin.core.qualifier.named
import org.koin.ktor.ext.inject

fun Application.configureSecurity() {
    val audience: String by inject<String>(named("jwt.audience"))
    val domain: String by inject<String>(named("jwt.domain"))
    val realm: String by inject<String>(named("jwt.realm"))
    val secret: String by inject<String>(named("jwt.secret"))

    authentication {
        jwt("auth-jwt") {
            this.realm = realm
            verifier(
                JWT
                    .require(Algorithm.HMAC256(secret))
                    .withAudience(audience)
                    .withIssuer(domain)
                    .build()
            )

            validate { credential ->
                val payload = credential.payload

                if (!payload.audience.contains(audience)) {
                    return@validate null
                }

                val now = Clock.System.now().toJavaInstant()
                if (payload.expiresAtAsInstant.isBefore(now)) {
                    return@validate null
                }

                return@validate JWTPrincipal(payload)
            }

            authSchemes("Bearer")
            authHeader { call ->
                val authentication = call.request.headers[AuthenticationHeader]

                return@authHeader if (authentication == null) {
                    null
                } else {
                    if (!authentication.startsWith("Bearer")) {
                        null
                    } else {
                        parseAuthorizationHeader(authentication)
                    }
                }
            }

            // when the jwt authentication failed (include `authHeader`), the `challenge` will be called
            challenge { defaultScheme, realm ->
                call.respond(
                    HttpStatusCode.Unauthorized,
                    Response.Err("token is not valid or has expired"))
            }
        }
    }
}
