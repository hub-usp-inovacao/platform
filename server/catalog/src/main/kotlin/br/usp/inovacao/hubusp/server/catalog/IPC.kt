package br.usp.inovacao.hubusp.server.catalog

import kotlinx.serialization.Serializable

@Serializable
data class IPC(
    val primary: String,
    val secondaries: Set<String>
)
