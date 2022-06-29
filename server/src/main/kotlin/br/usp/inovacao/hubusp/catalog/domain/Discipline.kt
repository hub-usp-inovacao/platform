package br.usp.inovacao.hubusp.catalog.domain

@kotlinx.serialization.Serializable
data class Discipline(
    val name: String,
    val categories: List<String>,
    val unity: String,
    val campus: String,
    val level: String,
    val nature: String
)
