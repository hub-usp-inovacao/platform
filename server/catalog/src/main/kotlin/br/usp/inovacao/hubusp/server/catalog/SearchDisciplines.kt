package br.usp.inovacao.hubusp.server.catalog

class SearchDisciplines(
    private val disciplineRepository: DisciplineRepository
) {
    fun search(params: Map<String, List<String>>): Set<Discipline> {
        return disciplineRepository.filter(params)
    }

    fun search(params: DisciplineSearchParams): Set<Discipline> {
        return disciplineRepository.filter(params)
    }
}
