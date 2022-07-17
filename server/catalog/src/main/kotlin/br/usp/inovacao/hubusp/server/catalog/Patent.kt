package br.usp.inovacao.hubusp.server.catalog

@kotlinx.serialization.Serializable
data class Patent(
    val classification: DualClassification,
    val countries_with_protection: Set<String>,
    val inventors: Set<String>,
    val ipcs: Set<String>,
    val name: String,
    val owners: Set<String>,
    val photo: String? = null,
    val status: String,
    val summary: String,
    val url: String? = null
)

@kotlinx.serialization.Serializable
data class DualClassification(
    val primary: Area,
    val secondary: Area? = null
)

@kotlinx.serialization.Serializable
data class Area(
    val cip: String,
    val subarea: String
)
