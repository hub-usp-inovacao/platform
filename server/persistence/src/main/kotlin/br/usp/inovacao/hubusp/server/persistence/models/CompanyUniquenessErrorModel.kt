package br.usp.inovacao.hubusp.server.persistence.models

@kotlinx.serialization.Serializable
data class CompanyUniquenessErrorModel(
    val error: String,
    val delivered: Boolean = false
)
