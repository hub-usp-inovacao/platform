package br.usp.inovacao.hubusp.curatorship.sheets

import kotlinx.serialization.Serializable

@Serializable
data class ResearcherValidationError(
    val errors: List<String>,
    val spreadsheetLineNumber: Int,
    val delivered: Boolean = false
)
