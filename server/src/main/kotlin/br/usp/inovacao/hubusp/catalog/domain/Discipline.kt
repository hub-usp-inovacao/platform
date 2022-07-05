package br.usp.inovacao.hubusp.catalog.domain

import kotlinx.serialization.Serializable

@Serializable
data class Category(
    val innovation: Boolean,
    val business: Boolean,
    val entrepreneurship: Boolean,
    val intellectual_property: Boolean
)

@Serializable
data class Discipline(
    val name: String,
    val category: Category,
    val unity: String,
    val campus: String,
    val level: String,
    val nature: String
)
