package br.usp.inovacao.hubusp.server.catalog

import kotlinx.serialization.Contextual
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder


@kotlinx.serialization.Serializable
data class PDI(
    val category: String,
    val name: String,
    val campus: String,
    val unity: String,
    val coordinator: String,
    val site: String,
    val email: String,
    val phone: String,
    val description: String,
    val tags: Set<String>,
)
