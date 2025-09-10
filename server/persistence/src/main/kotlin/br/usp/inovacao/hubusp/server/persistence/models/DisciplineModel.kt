package br.usp.inovacao.hubusp.server.persistence.models

import kotlinx.serialization.Serializable

@Serializable
data class DisciplineCategory(
    val innovation: Boolean,
    val business: Boolean,
    val entrepreneurship: Boolean,
    val intellectual_property: Boolean
)

@Serializable
data class DisciplineOffering(
    val classCode: String,
    val startDate: String,
    val endDate: String,
    val professors: Set<String> = emptySet(),
)

@Serializable
data class DisciplineModel(
    val campus: String,
    val category: DisciplineCategory,
    val description: String,
    val keywords: Set<String>,
    val level: String,
    val name: String,
    val nature: String,
    val offeringPeriod: String,
    val start_date: String,
    val unity: String,
    val url: String,
    val beingOffered: Boolean,
    val offerings: Set<DisciplineOffering> = emptySet()
)
