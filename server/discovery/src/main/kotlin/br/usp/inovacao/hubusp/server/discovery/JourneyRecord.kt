package br.usp.inovacao.hubusp.server.discovery

import kotlinx.serialization.Serializable

@Serializable
data class JourneyRecord(
    val name: String,
    val url: String? = null
)
