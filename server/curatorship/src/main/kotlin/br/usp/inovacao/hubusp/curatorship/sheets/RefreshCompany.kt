package br.usp.inovacao.hubusp.curatorship.sheets

import br.usp.inovacao.hubusp.curatorship.Mailer

private data class CompaniesAndErrors(
    val companies: Set<Company>,
    val errors: Set<ValidationError>
)

class RefreshCompany(
    private val mailer: Mailer,
    private val spreadsheetReader: SpreadsheetReader,
    private val companyRepository: CompanyRepository,
    private val companyErrorRepository: CompanyErrorRepository
) {
    companion object {
        private const val INDEX_CORRECTION_FACTOR = 2
        private const val THRESHOLD_FOR_CLEARING_COLLECTION = 1
    }

    // template method
    fun refresh() {
        try {
            readData()
                .let { validateData(it) }
                .let { storeData(it) }
        } catch (e: SheetReadingException) {
            mailer.notifySpreadsheetError(e.message)
        }
    }

    private fun readData() = spreadsheetReader.read(Sheets.Companies)

    private fun validateRow(index: Int, row: List<String?>) = try {
        Company.fromRow(row)
    } catch (ve: ValidationException) {
        ValidationError(
            errors = ve.messages,
            spreadsheetLineNumber = index + INDEX_CORRECTION_FACTOR
        )
    }

    private fun validateData(data: Matrix<String?>): CompaniesAndErrors {
        val groupsIsCompany = data
            .mapIndexed(this::validateRow)
            .groupBy { it is Company }

        val companies: Set<Company> = (groupsIsCompany[true]?.toSet() ?: emptySet()) as Set<Company>
        val errors: Set<ValidationError> = (groupsIsCompany[false]?.toSet() ?: emptySet()) as Set<ValidationError>

        return CompaniesAndErrors(companies, errors)
    }

    private fun storeData(companiesAndErrors: CompaniesAndErrors) {
        val (companies, errors) = companiesAndErrors

        if (companies.size >= THRESHOLD_FOR_CLEARING_COLLECTION) companyRepository.deleteAll()

        companies.forEach(companyRepository::save)
        errors.forEach(companyErrorRepository::save)
    }
}