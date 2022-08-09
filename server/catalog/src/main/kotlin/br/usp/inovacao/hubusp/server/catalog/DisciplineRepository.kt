package br.usp.inovacao.hubusp.server.catalog

interface DisciplineRepository {
    fun filter(params: DisciplineSearchParams): Set<Discipline>
}
