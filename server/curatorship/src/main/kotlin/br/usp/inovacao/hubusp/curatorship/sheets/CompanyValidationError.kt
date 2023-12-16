package br.usp.inovacao.hubusp.curatorship.sheets

import kotlinx.serialization.Serializable

@Serializable
data class CompanyValidationError(
    val errors: List<String>,
    val spreadsheetLineNumber: Int,
    val delivered: Boolean = false
)
