package br.usp.inovacao.hubusp.curatorship.sheets

import kotlinx.serialization.Serializable

@Serializable
data class DisciplineUniquenessError(
    val error: String,
    val delivered: Boolean = false
)
