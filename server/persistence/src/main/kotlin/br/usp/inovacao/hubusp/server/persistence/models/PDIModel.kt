package br.usp.inovacao.hubusp.server.persistence.models

import kotlinx.serialization.Serializable

@Serializable
data class PDIModel(
    val category: String,
    val name: String,
    val campus: String,
    val unity: String,
    val coordinator: String,
    val site: String,
    val email: String,
    val phone: String,
    val description: String,
    val tags: Set<String>
)
