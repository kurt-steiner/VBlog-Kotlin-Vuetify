package com.steiner.vblog.request

import kotlinx.serialization.Serializable

@Serializable
class PutUserRequest(
    val id: Int,
    val name: String? = null,
    val nickname: String? = null,
    val passwordHash: String? = null,
    val email: String? = null,
    val avatarId: Int? = null
)