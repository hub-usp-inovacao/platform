package br.usp.inovacao.hubusp.server.catalog

interface PDIRepository {

    fun filter(params: PDISearchParams): Set<PDI>
}