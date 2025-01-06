package com.steiner.vblog.request

import kotlinx.serialization.Serializable

@Serializable
class PostTagRequest(
    val name: String,
    val userId: Int
)