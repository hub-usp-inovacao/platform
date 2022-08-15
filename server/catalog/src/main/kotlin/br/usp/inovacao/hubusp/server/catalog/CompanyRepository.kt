package br.usp.inovacao.hubusp.server.catalog

interface CompanyRepository {
    /**
     * Filter companies
     *
     * Search for companies filtered by the params
     *
     * @param params - [CompanySearchParams] defining the matching criteria
     * @return Set<Company> - the set of [Company] records found
     */
    fun filter(params: CompanySearchParams): Set<Company>

    fun getEcosystems(): Set<String>

    fun getCities(): Set<String>
}
