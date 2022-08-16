package br.usp.inovacao.hubusp.server.catalog

data class CompanySearchParams(
    val areaMajors: Set<String> = emptySet(),
    val areaMinors: Set<String> = emptySet(),
    val city: String? = null,
    val ecosystem: String? = null,
    val size: String? = null,
    val term: String? = null,
)

