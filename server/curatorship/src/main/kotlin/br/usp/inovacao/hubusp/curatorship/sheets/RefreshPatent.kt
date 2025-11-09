package br.usp.inovacao.hubusp.curatorship.sheets

import br.usp.inovacao.hubusp.curatorship.Mailer

class RefreshPatent(
    val mailer: Mailer,
    val spreadsheetReader: SpreadsheetReader,
    val patentRepository: PatentRepository,
    val patentErrorRepository: PatentErrorRepository
){
    private val errorsList = mutableListOf<PatentValidationError>()

    companion object {
        private const val INDEX_CORRECTION_FACTOR = 2
    }

    private fun validateRow(rowIndex: Int, row: List<String?>) = try {
        Patent.fromRow(row)
    } catch (e: ValidationException) {
        PatentValidationError(
            errors = e.messages,
            spreadsheetLineNumber = rowIndex + INDEX_CORRECTION_FACTOR
        )
    }

    private fun persistValidData(data: Any) = when(data) {
        is Patent -> try {
            patentRepository.save(data)
        } catch (e: UniquenessException) {
            patentErrorRepository.save(PatentUniquenessError(error = e.message))
        }
        is PatentValidationError -> {
            patentErrorRepository.save(data)
            errorsList.add(data)
        }
        else -> throw RuntimeException("Error while persisting Patent: data isn't Patent nor PatentValidationError/PatentUniquenessError")
    }

    fun refresh() {
        try {
            val data = spreadsheetReader
                .read(Sheets.Patents)
                .drop(1)
                .mapIndexed(this::validateRow)
            if(data.filterIsInstance<Patent>().isNotEmpty()){
                patentRepository.clean()
                patentErrorRepository.clean()
                data.forEach(this::persistValidData)
                if(errorsList.isNotEmpty()){
                    val errorMail = errorsList.joinToString("\n") { validationError ->
                        val formattedErrors = validationError.errors.map { error ->
                            val property = error.substringBefore(": ")
                            val message = error.substringAfter(": ")
                            val column = Patent.propertyToColumn[property]

                            if (column != null) {
                                "Coluna $column ($property): $message"
                            } else {
                                error
                            }
                        }.joinToString(", ")
                        "Erros na linha ${validationError.spreadsheetLineNumber}: $formattedErrors"
                    }
                    mailer.notifySpreadsheetError("Foram encontrados erros na planilha de Patentes:\n\n${errorMail}", "Relatório Semanal - Patentes")
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
