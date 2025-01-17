package com.steiner.vblog.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
class Category(
    val id: Int,
    val name: String,
    val createTime: LocalDateTime,
    val userId: Int
)