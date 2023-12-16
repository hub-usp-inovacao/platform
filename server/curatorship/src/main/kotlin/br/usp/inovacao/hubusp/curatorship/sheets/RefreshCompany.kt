package br.usp.inovacao.hubusp.curatorship.sheets

import br.usp.inovacao.hubusp.curatorship.Mailer

class RefreshCompany(
    val mailer: Mailer,
    val spreadsheetReader: SpreadsheetReader,
    val companyRepository: CompanyRepository,
    val companyErrorRepository: CompanyErrorRepository
) {
    companion object {
        private const val INDEX_CORRECTION_FACTOR = 2
    }

    private fun validateRow(rowIndex: Int, row: List<String?>) = try {
        Company.fromRow(row)
    } catch (e: ValidationException) {
        CompanyValidationError(
            errors = e.messages,
            spreadsheetLineNumber = rowIndex + INDEX_CORRECTION_FACTOR
        )
    }

    private fun persistValidData(data: Any) = when(data) {
        is Company -> try {
            companyRepository.save(data)
        } catch (e: UniquenessException) {
            companyErrorRepository.save(CompanyUniquenessError(error = e.message))
        }
        is CompanyValidationError -> companyErrorRepository.save(data)
        else -> throw RuntimeException("Error while persisting Company: data isn't Company nor CompanyValidationError/CompanyUniquenessError")
    }

    fun refresh() {
        try {
            // TODO: delete "old" documents, when due
            val data = spreadsheetReader
                .read(Sheets.Companies)
                .drop(1)
                .mapIndexed(this::validateRow)
            if(data.filterIsInstance<Company>().isNotEmpty()){
                companyRepository.clean()
                data.forEach(this::persistValidData)
            }
            else{
                mailer.notifySpreadsheetError("Error while fetching the data: the new fetched data is Empty")
            }
        } catch (e: SheetReadingException) {
            mailer.notifySpreadsheetError(e.message)
        }
    }
}
