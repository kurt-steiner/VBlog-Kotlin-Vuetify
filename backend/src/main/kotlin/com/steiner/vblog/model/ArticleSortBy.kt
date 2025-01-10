package com.steiner.vblog.model

sealed class ArticleSortBy(open val reverse: Boolean) {
    class ByTitle(override val reverse: Boolean): ArticleSortBy(reverse)
    class ByEditTime(override val reverse: Boolean): ArticleSortBy(reverse)
    class ByPublishDate(override val reverse: Boolean): ArticleSortBy(reverse)
}