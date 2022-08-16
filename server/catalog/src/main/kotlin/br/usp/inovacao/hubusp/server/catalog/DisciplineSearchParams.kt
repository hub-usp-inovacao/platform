package br.usp.inovacao.hubusp.server.catalog

data class DisciplineSearchParams(
    val categories: Set<String> = emptySet(),
    val campus: String? = null,
    val unity: String? = null,
    val level: String? = null,
    val nature: String? = null,
    val term: String? = null,
)