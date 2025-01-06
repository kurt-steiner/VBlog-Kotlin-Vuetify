package com.steiner.vblog.model

import kotlinx.serialization.Serializable

@Serializable
class Tag(
    val id: Int,
    val name: String,
    val userId: Int
)