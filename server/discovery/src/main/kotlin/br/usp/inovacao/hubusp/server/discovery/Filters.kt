package br.usp.inovacao.hubusp.server.discovery

interface Filter

data class LearnStepFilters(
    val nature: String? = null,
    val level: String? = null
) : Filter

data class PracticeStepFilter(
    val category: String? = null
) : Filter

data class CreateStepFilter(
    val insideUSP: Boolean? = null
) : Filter

data class ImproveStepFilter(
    val category: String? = null
) : Filter

data class FundStepFilter(
    val type: String? = null
) : Filter
