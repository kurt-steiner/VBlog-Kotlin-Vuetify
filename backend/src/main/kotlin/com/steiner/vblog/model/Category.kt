package com.steiner.vblog.model

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable

@Serializable
class Category(
    val id: Int,
    val name: String,
    val createTime: LocalDateTime,
    val userId: Int
)