package com.steiner.vblog.request

import kotlinx.serialization.Serializable

@Serializable
class RegisterRequest(
    val name: String,
    val passwordHash: String,
    val nickname: String? = null,
    val email: String? = null,
    val avatarId: Int? = null
)