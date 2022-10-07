package br.usp.inovacao.hubusp.server.catalog

@kotlinx.serialization.Serializable
data class Company(
    val address: Address,
    val classification: Classification,
    val cnae: String,
    val companySize: Set<String>,
    val description: String,
    val ecosystems: Set<String>,
    val emails: Set<String>,
    val incubated: String,
    val logo: String? = null,
    val name: String,
    val phones: Set<String>,
    val services: Set<String>,
    val technologies: Set<String>,
    val unities: Set<String>,
    val url: String? = null
) {
    companion object {
        val INDEXABLE_PROPERTIES = listOf("description", "name", "services", "technologies")
    }

    fun matches(term: String): Boolean = description.contains(term) ||
            name.contains(term) ||
            services.any { it.contains(term) } ||
            technologies.any { it.contains(term)}
}

@kotlinx.serialization.Serializable
data class Classification(
    val major: String,
    val minor: String
)

@kotlinx.serialization.Serializable
data class Address(
    val cep: String,
    val city: Set<String>,
    val neighborhood: String,
    val state: String,
    val venue: String
)
