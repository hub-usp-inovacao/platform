package br.usp.inovacao.hubusp.server.catalog

class SearchInitiatives(
    private val initiativeRepository: InitiativeRepository
) {
    fun search(params: InitiativeSearchParams) = initiativeRepository.filter(params)
}