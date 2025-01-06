package com.steiner.vblog.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
class ImageItem(
    val id: Int,
    val name: String,

    @Transient
    val path: String = ""
)