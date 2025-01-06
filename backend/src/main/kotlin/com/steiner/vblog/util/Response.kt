package com.steiner.vblog.util

import kotlinx.serialization.Serializable

sealed class Response {
    @Serializable
    data class Ok<T: Any>(val message: String, val data: T): Response()

    @Serializable
    data class Err(val message: String): Response()
}