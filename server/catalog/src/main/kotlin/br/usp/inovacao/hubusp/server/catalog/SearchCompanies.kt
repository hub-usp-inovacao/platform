package br.usp.inovacao.hubusp.server.catalog

class SearchCompanies(
    private val companyRepository: CompanyRepository
) {
    fun search(params: CompanySearchParams) = companyRepository.filter(params)

    fun getAllEcosystems(): Set<String> = companyRepository.getEcosystems()

    fun getAllCities(): Set<String> = companyRepository.getCities()
}
