package com.steiner.vblog.request

import com.steiner.vblog.model.Tag
import kotlinx.serialization.Serializable

@Serializable
class PutArticleRequest(
    val id: Int,
    val title: String? = null,
    val markdownContent: String? = null,
    val htmlContent: String? = null,
    val categoryId: Int? = null,
    val tags: List<Tag>? = null,
)