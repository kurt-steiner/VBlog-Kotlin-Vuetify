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
    val status: ArticleStatus,
    val tags: List<Tag>
) {
    companion object {
        @JvmStatic
        fun fromArticle(article: Article): ArticleShortcut {
            return ArticleShortcut(
                id = article.id,
                title = article.title,
                summary = article.summary,
                category = article.category,
                author = article.author,
                publishDate = article.publishDate,
                editTime = article.editTime,
                status = article.status,
                tags = article.tags
            )
        }
    }

}