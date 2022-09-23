package br.usp.inovacao.hubusp.curatorship.sheets

import br.usp.inovacao.hubusp.curatorship.Mailer

class RefreshPDI(
    val mailer: Mailer,
    val spreadsheetReader: SpreadsheetReader,
    val pdiRepository: PDIRepository,
    val pdiErrorRepository: PDIErrorRepository
) {
    companion object {
        private const val INDEX_CORRECTION_FACTOR = 2
    }

    private fun validateRow(rowIndex: Int, row: List<String?>) = try {
        PDI.fromRow(row)
    } catch (e: ValidationException) {
        PDIValidationError(
            errors = e.messages,
            spreadsheetLineNumber = rowIndex + INDEX_CORRECTION_FACTOR
        )
    }

    private fun persistValidData(data: Any) = when(data) {
        is PDI -> pdiRepository.save(data)
        is PDIValidationError -> pdiErrorRepository.save(data)
        else -> throw RuntimeException("Error while persisting PDI: data isn't PDI nor PDIValidationError")
    }

    private fun deleteIfNecessary(pdisOrErrors: List<Any>) {
        val atLeastOnePDI = pdisOrErrors.any { it is PDI }
        if (atLeastOnePDI) pdiRepository.deleteAll()
    }

    fun refresh() {
        try {
            val pdisOrErrors = spreadsheetReader
                .read(Sheets.PDIs)
                .mapIndexed(this::validateRow)

            deleteIfNecessary(pdisOrErrors)

            pdisOrErrors
                .forEach(this::persistValidData)
        } catch (e: SheetReadingException) {
            mailer.notifySpreadsheetError(e.message)
        }
    }

}