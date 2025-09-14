package br.usp.inovacao.hubusp.curatorship.sheets

import br.usp.inovacao.hubusp.curatorship.Mailer

class RefreshCompany(
    val mailer: Mailer,
    val spreadsheetReader: SpreadsheetReader,
    val companyRepository: CompanyRepository,
    val companyErrorRepository: CompanyErrorRepository
) {
    companion object {
        /** we skip the first row (headers) and google sheets is 1-indexed */
        private const val INDEX_CORRECTION_FACTOR = 2
    }

    private fun persistData(rowIndex: Int, company: Company) {
        try {
            companyRepository.save(company)
        } catch (e: UniquenessException) {
            // TODO: Show line number
            companyErrorRepository.save(CompanyUniquenessError(error = e.message))
        }

        try {
            company.validate()
        } catch (e: ValidationException) {
            companyErrorRepository.save(
                CompanyValidationError(
                    errors = e.messages,
                    spreadsheetLineNumber = rowIndex + INDEX_CORRECTION_FACTOR,
                ),
            )
        }
    }

    fun refresh() {
        try {
            // TODO: delete "old" documents, when due
            val companyRows = spreadsheetReader.read(Sheets.Companies).drop(1).map(Company::fromRow)

            if (companyRows.isNotEmpty()) {
                companyRepository.clean()
                companyErrorRepository.clean()
                companyRows.forEachIndexed(this::persistData)
            } else {
                mailer.notifySpreadsheetError(
                    "Error while fetching the data: the new fetched data is Empty",
                )
            }
        } catch (e: SheetReadingException) {
            mailer.notifySpreadsheetError(e.message)
        }
    }
}
