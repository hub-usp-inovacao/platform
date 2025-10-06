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
            val data = spreadsheetReader
                .read(Sheets.PDIs)
                .drop(1)
                .mapIndexed(this::validateRow)
            if(data.filterIsInstance<PDI>().isNotEmpty()){
                pdiRepository.clean()
                pdiErrorRepository.clean()
                data.forEach(this::persistValidData)
                if(errorsList.isNotEmpty()){
                    val errorMail = errorsList.joinToString("\n") { validationError ->
                        val formattedErrors = validationError.errors.map { error ->
                            val property = error.substringBefore(": ")
                            val message = error.substringAfter(": ")
                            val column = PDI.propertyToColumn[property]

                            if (column != null) {
                                "Coluna $column ($property): $message"
                            } else {
                                error
                            }
                        }.joinToString(", ")
                        "Erros na linha ${validationError.spreadsheetLineNumber}: $formattedErrors"
                    }
                    mailer.notifySpreadsheetError("Foram encontrados erros na planilha de PDI:\n\n${errorMail}", "Relatório Semanal - P&D&I")
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