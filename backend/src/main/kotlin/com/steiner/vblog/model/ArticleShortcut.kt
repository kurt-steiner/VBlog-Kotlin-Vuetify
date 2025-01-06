package com.steiner.vblog.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
class ArticleShortcut(
    val id: Int,
    val title: String,
    val summary: String,
    val category: Category?,
    val author: User,
    val publishDate: LocalDateTime,
    val editTime: LocalDateTime,
    val status: Article.Companion.Status
)