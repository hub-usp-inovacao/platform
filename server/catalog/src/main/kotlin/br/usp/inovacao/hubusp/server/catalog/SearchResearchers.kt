package br.usp.inovacao.hubusp.server.catalog

class SearchResearchers(
    private val researcherRepository: ResearcherRepository
) {
    fun search(params: ResearcherSearchParams) = researcherRepository.filter(params)
}
