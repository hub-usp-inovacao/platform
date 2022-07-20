package br.usp.inovacao.hubusp.server.catalog

@kotlinx.serialization.Serializable
data class PDI(
    val category: String,
    val name: String,
    val campus: String,
    val unity: String,
    val coordinator: String,
    val site: String,
    val email: String,
    val phone: String,
    val description: String,
    val tags: Set<String>,
)
