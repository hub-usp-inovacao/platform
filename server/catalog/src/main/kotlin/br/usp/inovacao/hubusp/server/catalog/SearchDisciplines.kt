package br.usp.inovacao.hubusp.server.catalog

class SearchDisciplines(
    private val disciplineRepository: DisciplineRepository
) {
    fun search(params: DisciplineSearchParams): Set<Discipline> {
        return disciplineRepository.filter(params)
    }
}
