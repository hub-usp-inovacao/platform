package br.usp.inovacao.hubusp.curatorship.sheets

import kotlinx.serialization.Serializable

@Serializable
data class ValidationError(
    val errors: Iterable<String>,
    val spreadsheetLineNumber: Int,
    val delivered: Boolean = false
)

