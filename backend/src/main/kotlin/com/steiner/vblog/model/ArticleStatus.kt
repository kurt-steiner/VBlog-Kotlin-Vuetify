package com.steiner.vblog.model

import com.steiner.vblog.util.ArticleStatusSerializer
import kotlinx.serialization.Serializable

@Serializable(with = ArticleStatusSerializer::class)
enum class ArticleStatus(val code: Int) {
    Draft(0),
    Published(1),
    Dustbin(2),
    Deleted(3)
}
