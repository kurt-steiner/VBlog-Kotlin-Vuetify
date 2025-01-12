package com.steiner.vblog.model

enum class SortBy {
    ByTitle,
    ByPublishDate,
    ByEditTime
}

class ArticleSortBy(
    val sortBy: SortBy,
    val reverse: Boolean
)