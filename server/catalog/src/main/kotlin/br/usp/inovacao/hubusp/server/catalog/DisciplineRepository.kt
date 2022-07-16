package br.usp.inovacao.hubusp.server.catalog

interface DisciplineRepository {
    /**
     * Filters Disciplines from the datasource
     *
     * Fetches a filtered set of Disciplines from the underlying datasource
     *
     * @param params - the representation of the filter; provide an `emptyMap()` to fetch all
     * @return Set<Discipline> - the representing set of Disciplines that matches provided filter
     */
    fun filter(params: Map<String, List<String>>): Set<Discipline>
}
