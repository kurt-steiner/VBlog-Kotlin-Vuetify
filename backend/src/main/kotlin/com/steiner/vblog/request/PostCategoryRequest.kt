package com.steiner.vblog.request

import kotlinx.serialization.Serializable

@Serializable
class PostCategoryRequest(
    val name: String,
    val userId: Int
)