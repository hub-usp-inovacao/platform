package br.usp.inovacao.hubusp.server.catalog

interface PatentRepository {
    /**
     * Filters Patents from the datasource
     *
     * Fetches a filtered set of Patents from the underlying datasource
     *
     * @param params - [PatentSearchParams] defining the matching criteria
     * @return Set<Patent> - the set of [Patent] records found
     */
    fun filter(params: PatentSearchParams): Set<Patent>
}