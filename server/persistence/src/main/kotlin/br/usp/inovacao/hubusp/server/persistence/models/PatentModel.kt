package br.usp.inovacao.hubusp.server.persistence.models

@kotlinx.serialization.Serializable
data class PatentModel(
    val classification: PatentDualClassificationModel,
    val countriesWithProtection: Set<String>,
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
data class PatentDualClassificationModel(
    val primary: PatentAreaModel,
    val secondary: PatentAreaModel? = null
)

@kotlinx.serialization.Serializable
data class PatentAreaModel(
    val cip: String,
    val subarea: String
)
