package com.steiner.vblog.service

import com.steiner.vblog.model.ImageItem
import com.steiner.vblog.table.ImageItems
import com.steiner.vblog.table.Users
import com.steiner.vblog.util.dbQuery
import io.ktor.http.content.*
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import java.nio.file.Paths
import kotlin.io.path.absolutePathString

class ImageItemService(val database: Database): KoinComponent {
    init {
        transaction(database) {
            SchemaUtils.create(ImageItems)
        }
    }

    val storagePath: String by inject<String>(named("storage"))

    suspend fun insertOne(file: PartData.FileItem): ImageItem = dbQuery(database) {
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val filename = file.originalFileName ?: "untitled"
        val filenameEncoded = "$filename-${now.year}-${now.month}-${now.dayOfMonth}-${now.hour}-${now.minute}-${now.minute}"
        val path = Paths.get(storagePath, filenameEncoded).absolutePathString()

        val id = with (ImageItems) {
            insert {
                it[name] = filenameEncoded
                it[this.path] = path
            } get id
        }



        findOne(id.value)!!
    }

    suspend fun findOne(id: Int): ImageItem? = dbQuery(database) {
        with (ImageItems) {
            selectAll().where(this.id eq id)
                .firstOrNull()?.let {
                    ImageItem(
                        id = it[this.id].value,
                        name = it[name],
                        path = it[path]
                    )
                }
        }
    }

    suspend fun clear() = dbQuery(database) {
        with (ImageItems) {
            selectAll().map {
                it[id].value
            }.forEach { avatarId ->
                with (Users) {
                    update({Users.avatarId eq avatarId}) {
                        it[this.avatarId] = null
                    }
                }
            }

            deleteAll()
        }

    }
}