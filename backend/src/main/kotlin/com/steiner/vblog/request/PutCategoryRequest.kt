package com.steiner.vblog.request

import kotlinx.serialization.Serializable

@Serializable
class PutCategoryRequest(
    val id: Int,
    val name: String
)