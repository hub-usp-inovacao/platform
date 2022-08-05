package br.usp.inovacao.hubusp.server.catalog

data class PDISearchParams(
    val categories: Set<String> = emptySet(),
    val campus: String? = null,
    val term: String? = null,
)