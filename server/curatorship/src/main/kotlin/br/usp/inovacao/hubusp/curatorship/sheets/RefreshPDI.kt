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
        private val errorsList = mutableListOf<PDIValidationError>()
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
        is PDIValidationError -> {
            pdiErrorRepository.save(data)
            errorsList.add(data)
        }
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
                pdiRepository.clean()
                pdiErrorRepository.clean()
                data.forEach(this::persistValidData)
                if(errorsList.isNotEmpty()){
                    val errorMail = errorsList.joinToString("\n") { "Erros na linha ${it.spreadsheetLineNumber}: ${it.errors.joinToString(", ")}" }
                    mailer.notifySpreadsheetError("Foram encontrados erros na planilha de PDI:\n\n${errorMail}")
                }
            }
            else{
                mailer.notifySpreadsheetError("Error while fetching the data: the new fetched data is Empty")
            }
        } catch (e: SheetReadingException) {
            mailer.notifySpreadsheetError(e.message)
        }

    }
}