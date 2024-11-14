package br.usp.inovacao.hubusp.server.catalog

@kotlinx.serialization.Serializable
data class Initiative(
    val classification: String,
    val contact: Contact,
    val description: String,
    val email: Set<String>? = null,
    val localization: String,
    val name: String,
    val tags: Set<String>,
    val unity: String,
    val url: String? = null,
)

@kotlinx.serialization.Serializable
data class Contact(
    val info: String,
    val person: String
)
