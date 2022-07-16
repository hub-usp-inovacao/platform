package br.usp.inovacao.hubusp.server.catalog

interface ResearcherRepository {
    /**
     * Filters researchers
     *
     * Search for researchers filtered by the params
     *
     * @param params - [ResearcherSearchParams] defining the matching criteria
     * @return Set<Researcher> - the set of [Researcher] records found
     */
    fun filter(params: ResearcherSearchParams): Set<Researcher>
}
