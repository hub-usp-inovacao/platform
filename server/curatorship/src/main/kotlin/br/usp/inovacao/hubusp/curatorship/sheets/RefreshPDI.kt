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

    fun refresh() {
        try {
            // TODO: delete "old" documents, when due
            val data = spreadsheetReader
                .read(Sheets.PDIs)
                .drop(1)
                .mapIndexed(this::validateRow)
            if(data.filterIsInstance<PDI>().isNotEmpty()){
                data.forEach(this::persistValidData)
            }
        } catch (e: SheetReadingException) {
            mailer.notifySpreadsheetError(e.message)
        }

    }
}