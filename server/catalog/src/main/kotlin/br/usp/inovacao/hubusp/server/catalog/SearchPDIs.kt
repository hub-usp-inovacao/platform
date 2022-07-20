package br.usp.inovacao.hubusp.server.catalog

class SearchPDIs(private val pdiRepository: PDIRepository) {

    fun search(params: PDISearchParams): Set<PDI> {
        return pdiRepository.filter(params)
    }
}