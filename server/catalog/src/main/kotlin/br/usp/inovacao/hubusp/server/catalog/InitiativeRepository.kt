package br.usp.inovacao.hubusp.server.catalog

interface InitiativeRepository {
    /**
     * Filters Initiatives
     *
     * Search for initiatives filtered by the params
     *
     * @param params - [InitiativeSearchParams] defining the matching criteria
     * @return Set<Initiative> - the set of [Initiative] records found
     */
    fun filter(params: InitiativeSearchParams): Set<Initiative>
}