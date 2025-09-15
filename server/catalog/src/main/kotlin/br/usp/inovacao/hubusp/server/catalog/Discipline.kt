package br.usp.inovacao.hubusp.server.catalog

@kotlinx.serialization.Serializable
data class Category(
    val innovation: Boolean,
    val business: Boolean,
    val entrepreneurship: Boolean,
    val intellectual_property: Boolean
)

@kotlinx.serialization.Serializable
data class Offering(
    val classCode: String,
    val startDate: String,
    val endDate: String,
    val professors: Set<String> = emptySet(),
)

@kotlinx.serialization.Serializable
data class Discipline(
    val name: String,
    val category: Category,
    val description: String,
    val unity: String,
    val campus: String,
    val level: String,
    val nature: String,
    val url: String,
    val offeringPeriod: String,
    val beingOffered: Boolean,
    val offerings: Set<Offering> = emptySet(),
)
