package com.steiner.vblog.model

class ArticleQuery(
    val authorId: Int,
    val status: ArticleStatus?,
    val tagId: Int?,
    val categoryId: Int?,
    val title: String?,
    val sortBy: ArticleSortBy?
)