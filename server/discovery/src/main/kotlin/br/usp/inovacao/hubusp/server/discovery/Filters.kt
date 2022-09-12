package br.usp.inovacao.hubusp.server.discovery

interface Filter

data class LearnStepFilters(
    val nature: String,
    val level: String
) : Filter

data class PracticeStepFilter(
    val category: String
) : Filter

data class CreateStepFilter(
    val insideUSP: Boolean
) : Filter

data class ImproveStepFilter(
    val category: String
) : Filter

data class FundStepFilter(
    val type: String
) : Filter
