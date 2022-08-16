package br.usp.inovacao.hubusp.server.catalog

class SearchPatents(
    private val patentRepository: PatentRepository
) {
    fun search(params: PatentSearchParams) = patentRepository.filter(params)

    fun getAllClassifications() = patentRepository.getClassifications()
}