package br.usp.inovacao.hubusp.server.persistence.models

@kotlinx.serialization.Serializable
data class ResearcherUniquenessErrorModel(
    val error: String,
    val delivered: Boolean = false
)
