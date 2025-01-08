package com.steiner.vblog.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class ArticleQuery {
    @Serializable
    @SerialName("author")
    class Author(val authorId: Int): ArticleQuery()

    @Serializable
    @SerialName("status")
    class Status(val authorId: Int, val status: ArticleStatus): ArticleQuery()

    @Serializable
    @SerialName("title")
    class Title(val authorId: Int, val title: String): ArticleQuery()
}