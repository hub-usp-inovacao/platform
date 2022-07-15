package br.usp.inovacao.hubusp.server.catalog

interface DisciplineRepository {
    fun filter(params: Map<String, List<String>>): Set<Discipline>
}
