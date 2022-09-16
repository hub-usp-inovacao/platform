package br.usp.inovacao.hubusp.server.persistence.models

@kotlinx.serialization.Serializable
data class PDIValidationErrorModel(
    val errors: Iterable<String>,
    val spreadsheetLineNumber: Int,
    val delivered: Boolean = false
)
