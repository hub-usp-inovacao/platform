package br.usp.inovacao.hubusp.server.persistence.models

@kotlinx.serialization.Serializable
data class InitiativeUniquenessErrorModel(
    val error: String,
    val delivered: Boolean = false
)
