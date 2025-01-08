package com.steiner.vblog.model

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
    val status: ArticleStatus,
    val tags: List<Tag>
)