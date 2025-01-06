package com.steiner.vblog.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
class User(
    val id: Int,
    val name: String,
    val nickname: String?,
    val email: String?,
    val registerTime: LocalDateTime,
    val avatar: ImageItem?
)