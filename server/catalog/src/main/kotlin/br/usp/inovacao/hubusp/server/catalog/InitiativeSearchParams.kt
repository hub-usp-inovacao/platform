package br.usp.inovacao.hubusp.server.catalog

data class InitiativeSearchParams(
    val classifications: Set<String> = emptySet(),
    val campus: String? = null,
    val term: String? = null,
)
