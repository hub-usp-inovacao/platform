package br.usp.inovacao.hubusp.server.catalog

class DisciplineService(
    private val disciplineRepository: DisciplineRepository
) {
    fun search(params: Map<String, List<String>>): Set<Discipline> {
        return disciplineRepository.filter(params)
    }
}
