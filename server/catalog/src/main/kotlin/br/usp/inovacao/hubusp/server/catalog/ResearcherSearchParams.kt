package br.usp.inovacao.hubusp.server.catalog

data class ResearcherSearchParams(
    val majorArea: Set<String> = emptySet(),
    val minorArea: Set<String> = emptySet(),
    val campus: String? = null,
    val unity: String? = null,
    val bond: String? = null,
    val term: String? = null,
)
