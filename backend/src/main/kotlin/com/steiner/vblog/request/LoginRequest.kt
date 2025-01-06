package com.steiner.vblog.request

import kotlinx.serialization.Serializable

@Serializable
class LoginRequest(
    val name: String,
    val passwordHash: String
)