package br.usp.inovacao.hubusp.server.catalog

data class PatentSearchParams(
    val majorAreas: Set<String> = emptySet(),
    val minorAreas: Set<String> = emptySet(),
    val status: String? = null,
    val term: String? = null,
)
