package com.steiner.vblog.model

sealed class ArticleQuery(open val authorId: Int, open val sortBy: ArticleSortBy) {
    class Author(
        override val authorId: Int,
        override val sortBy: ArticleSortBy): ArticleQuery(authorId, sortBy)

    class Status(
        override val authorId: Int,
        override val sortBy: ArticleSortBy,
        val status: ArticleStatus): ArticleQuery(authorId, sortBy)

    class Title(
        override val authorId: Int,
        override val sortBy: ArticleSortBy,
        val title: String): ArticleQuery(authorId, sortBy)

    class Tag(
        override val authorId: Int,
        override val sortBy: ArticleSortBy,
        val tagId: Int): ArticleQuery(authorId, sortBy)

}