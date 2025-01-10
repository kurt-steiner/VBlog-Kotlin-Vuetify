package com.steiner.vblog.route

import com.steiner.vblog.service.ImageItemService
import com.steiner.vblog.util.Response
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import io.ktor.utils.io.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.io.files.FileNotFoundException
import kotlinx.io.readByteArray
import org.koin.ktor.ext.inject
import java.io.File

fun Application.routeImage() {
    val imageItemService: ImageItemService by inject<ImageItemService>()

    routing {
        route("/image") {
            get("/download/{id}") {
                val id = call.pathParameters.getOrFail<Int>("id")
                val imageItem = imageItemService.findOne(id)
                    ?: throw BadRequestException("no such image item with id $id")

                val file = File(imageItem.path)

                if (!file.exists()) {
                    throw FileNotFoundException("file not exist")
                }

                call.respondFile(file)
            }

            post("/upload") {
                val multipartData = call.receiveMultipart()

                multipartData.forEachPart { part ->
                    if (part is PartData.FileItem) {
                        val result = imageItemService.insertOne(part)
                        val fileBytes = part.provider().readRemaining().readByteArray()

                        withContext(Dispatchers.IO) {
                            File(result.path).writeBytes(fileBytes)
                        }

                        part.dispose()

                        call.respond(Response.Ok("upload ok", result))
                    }
                }
            }
        }
    }

}