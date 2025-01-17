package com.steiner.vblog.util

import com.steiner.vblog.model.ArticleStatus
import io.ktor.server.plugins.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object ArticleStatusSerializer: KSerializer<ArticleStatus> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("ArticleStatus", PrimitiveKind.INT)

    override fun serialize(encoder: Encoder, value: ArticleStatus) {
        encoder.encodeInt(value.code)
    }

    override fun deserialize(decoder: Decoder): ArticleStatus {
        return when (decoder.decodeInt()) {
            0 -> ArticleStatus.Draft
            1 -> ArticleStatus.Published
            2 -> ArticleStatus.Dustbin
            else -> throw BadRequestException("invalid status code")
        }
    }
}