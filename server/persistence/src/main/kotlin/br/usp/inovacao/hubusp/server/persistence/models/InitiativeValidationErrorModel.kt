package br.usp.inovacao.hubusp.server.persistence.models

@kotlinx.serialization.Serializable
data class InitiativeValidationErrorModel(
    val errors: List<String>,
    val spreadsheetLineNumber: Int,
    val delivered: Boolean = false
)
