package br.usp.inovacao.hubusp.curatorship.sheets

interface CompanyRepository {
    fun save(company:Company)
    fun clean()
}
