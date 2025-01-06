package com.steiner.vblog.model

import io.ktor.server.plugins.*
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
class Article(
    val id: Int,
    val title: String,
    val markdownContent: String,
    val htmlContent: String,
    val summary: String,
    val category: Category?,
    val author: User,
    val publishDate: LocalDateTime,
    val editTime: LocalDateTime,
    val status: Article.Companion.Status,
    val tags: List<Tag>
) {
    companion object {
        enum class Status(val code: Int) {
            Draft(0),
            Published(1),
            Dustbin(2),
            Deleted(3);
        }

        @JvmStatic
        fun decodeStatus(code: Int): Status {
            return when (code) {
                0 -> Status.Draft
                1 -> Status.Published
                2 -> Status.Dustbin
                3 -> Status.Deleted
                else -> throw BadRequestException("unknown status code $code")
            }
        }
    }

}