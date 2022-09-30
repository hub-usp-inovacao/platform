package br.usp.inovacao.hubusp.curatorship.sheets

interface CompanyErrorRepository {
    fun save(companyError: ValidationError)
}