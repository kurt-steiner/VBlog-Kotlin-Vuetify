package com.steiner

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.steiner.vblog.AuthenticationHeader
import com.steiner.vblog.model.User
import com.steiner.vblog.request.*
import com.steiner.vblog.util.Response
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.http.auth.*
import io.ktor.http.parsing.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.runBlocking
import kotlin.test.Test


class ApplicationTest {
    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json()
        }
    }

    @Test
    fun testRegister(): Unit = runBlocking {
        val response = client.post("http://localhost:8080/register") {
            contentType(ContentType.Application.Json)

            setBody(RegisterRequest(
                name = "steiner",
                passwordHash = "123456",
            ))
        }

        println(response.status)
        println(response.bodyAsText())
    }

    @Test
    fun testFakeDataInject(): Unit = runBlocking {
        val loginRespone = client.post("http://localhost:8080/login") {
            contentType(ContentType.Application.Json)

            setBody(LoginRequest(
                name = "steiner",
                passwordHash = "123456"
            ))
        }

        val authentication = "Bearer ${loginRespone.body<Response.Ok<String>>().data}"
        val getUserResponse = client.get("http://localhost:8080/user") {
            header(AuthenticationHeader, authentication)
        }

        val user = getUserResponse.body<Response.Ok<User>>().data

        client.post("http://localhost:8080/category") {
            contentType(ContentType.Application.Json)
            header(AuthenticationHeader, authentication)
            setBody(PostCategoryRequest(
                name = "分类2",
                userId = user.id
            ))

        }

        client.post("http://localhost:8080/tag") {
            contentType(ContentType.Application.Json)
            header(AuthenticationHeader, authentication)
            setBody(PostTagRequest(
                name = "tag1",
                userId = user.id
            ))
        }

    }

    @Test
    fun testPostArticle(): Unit = runBlocking {
        val loginRespone = client.post("http://localhost:8080/login") {
            contentType(ContentType.Application.Json)

            setBody(LoginRequest(
                name = "steiner",
                passwordHash = "123456"
            ))
        }

        val authentication = "Bearer ${loginRespone.body<Response.Ok<String>>().data}"
        val getUserResponse = client.get("http://localhost:8080/user") {
            header(AuthenticationHeader, authentication)
        }

        val user = getUserResponse.body<Response.Ok<User>>().data

        client.post("http://localhost:8080/article") {
            contentType(ContentType.Application.Json)
            header(AuthenticationHeader, authentication)
            setBody(PostArticleRequest(
                title = "hello",
                htmlContent = "<h1> Hello World </h1>",
                markdownContent = "# Hello World",
                status = 1,
                authorId = user.id,
            ))
        }
    }


    @Test
    fun testParseToken() {
        try {
            val token =
                "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJqd3QtYXVkaWVuY2UiLCJpc3MiOiJodHRwczovL2p3dC1wcm92aWRlci1kb21haW4vIiwidXNlcm5hbWUiOiJzdGVpbmVyIiwiZXhwIjoxNzM2MjM3MzUzfQ.Wg0SdG_x5uKvYU8Dc6O8LweAZ4tkkEd1QzEmQc_JKPA"
            val result = parseAuthorizationHeader(token.substring("Bearer ".length))
            println(result)
        } catch (e: ParseException) {
            println(e.message)
        }
    }

    @Test
    fun testVerifiyToken() {
        val token =
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJqd3QtYXVkaWVuY2UiLCJpc3MiOiJodHRwczovL2p3dC1wcm92aWRlci1kb21haW4vIiwidXNlcm5hbWUiOiJzdGVpbmVyIiwiZXhwIjoxNzM2MjM3MzUzfQ.Wg0SdG_x5uKvYU8Dc6O8LweAZ4tkkEd1QzEmQc_JKPA"
        val secret = "secret"
        val audience = "jwt-audience"
        val domain = "https://jwt-provider-domain/"

        val verifier = JWT.require(Algorithm.HMAC256(secret))
            .withAudience(audience)
            .withIssuer(domain)
            .build()
        try {
            val result = verifier.verify(token)
            println(result)
        } catch (e: Exception) {
            println("${e.message}")
        }

    }
}
