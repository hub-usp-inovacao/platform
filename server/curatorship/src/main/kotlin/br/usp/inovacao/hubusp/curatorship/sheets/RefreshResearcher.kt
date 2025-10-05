package br.usp.inovacao.hubusp.curatorship.sheets

import br.usp.inovacao.hubusp.curatorship.Mailer

class RefreshResearcher(
    val mailer: Mailer,
    val spreadsheetReader: SpreadsheetReader,
    val researcherRepository: ResearcherRepository,
    val researcherErrorRepository: ResearcherErrorRepository
) {
    companion object {
        private const val INDEX_CORRECTION_FACTOR = 2
        private val errorsList = mutableListOf<ResearcherValidationError>()
    }

    private fun validateRow(rowIndex: Int, row: List<String?>) = try {
        Researcher.createFrom(row)
    } catch (e: ValidationException) {
        ResearcherValidationError(
            errors = e.messages,
            spreadsheetLineNumber = rowIndex + INDEX_CORRECTION_FACTOR
        )
    }

    private fun persistValidData(data: Any) = when(data) {
        is Researcher -> try {
            researcherRepository.save(data)
        } catch (e: UniquenessException) {
            researcherErrorRepository.save(ResearcherUniquenessError(error = e.message))
        }
        is ResearcherValidationError -> {
            researcherErrorRepository.save(data)
            errorsList.add(data)
        }
        else -> throw RuntimeException("Error while persisting Researcher: data isn't Researcher nor ResearcherValidationError/ResearcherUniquenessError")
    }

    fun refresh() {
        try {
            val data = spreadsheetReader
                .read(Sheets.Researchers)
                .drop(1)
                .mapIndexed(this::validateRow)
            if(data.filterIsInstance<Researcher>().isNotEmpty()){
                researcherRepository.clean()
                researcherErrorRepository.clean()
                data.forEach(this::persistValidData)
                if(errorsList.isNotEmpty()){
                    val errorMail = errorsList.joinToString("\n") { validationError ->
                        val formattedErrors = validationError.errors.map { error ->
                            val property = error.substringBefore(": ")
                            val message = error.substringAfter(": ")
                            val column = Researcher.propertyToColumn[property]

                            if (column != null) {
                                "Coluna $column ($property): $message"
                            } else {
                                error
                            }
                        }.joinToString(", ")
                        "Erros na linha ${validationError.spreadsheetLineNumber}: $formattedErrors"
                    }
                    mailer.notifySpreadsheetError("Foram encontrados erros na planilha de Competências:\n\n${errorMail}", "Relatório Semanal - Competências")
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
