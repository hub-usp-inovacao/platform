package br.usp.inovacao.hubusp.server.catalog

@kotlinx.serialization.Serializable
data class Category(
    val innovation: Boolean,
    val business: Boolean,
    val entrepreneurship: Boolean,
    val intellectual_property: Boolean
)

@kotlinx.serialization.Serializable
data class Description(
    val long: String,
    val short: String,
)

@kotlinx.serialization.Serializable
data class Discipline(
    val name: String,
    val category: Category,
    val description: Description,
    val unity: String,
    val campus: String,
    val level: String,
    val nature: String,
    val url: String
)
