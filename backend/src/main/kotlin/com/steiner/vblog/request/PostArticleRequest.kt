package com.steiner.vblog.request

import com.steiner.vblog.model.ArticleStatus
import com.steiner.vblog.model.Tag
import kotlinx.serialization.Serializable

@Serializable
class PostArticleRequest(
    val title: String,
    val markdownContent: String,
    val htmlContent: String,
    val categoryId: Int? = null,
    val tags: List<Tag>? = null,
    val status: ArticleStatus,
    val authorId: Int
)