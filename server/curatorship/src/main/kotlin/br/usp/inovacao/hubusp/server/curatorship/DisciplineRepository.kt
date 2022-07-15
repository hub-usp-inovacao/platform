package br.usp.inovacao.hubusp.server.curatorship

interface DisciplineRepository {
    fun batchRefresh(disciplines: Iterable<Discipline>)
}
