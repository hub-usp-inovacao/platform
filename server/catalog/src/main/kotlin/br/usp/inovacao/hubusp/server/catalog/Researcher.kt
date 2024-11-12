package br.usp.inovacao.hubusp.server.catalog

@kotlinx.serialization.Serializable
data class Researcher(
    val name: String,
    val email: String,
    val unities: Set<String>,
    val campus: String,
    val skills: Set<String>,
    val equipments: Set<String>,
    val services: Set<String>,
    val area: KnowledgeAreas,
    val keywords: Set<String>,
    val lattes: String,
    val photo: String? = null,
    val phone: String? = null,
    val bond: String,
)

@kotlinx.serialization.Serializable
data class KnowledgeAreas(
    val major: Set<String>,
    val minors: Set<String>
)
