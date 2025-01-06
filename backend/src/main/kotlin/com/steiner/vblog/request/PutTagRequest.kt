package com.steiner.vblog.request

import kotlinx.serialization.Serializable

@Serializable
class PutTagRequest(
    val id: Int,
    val name: String
)