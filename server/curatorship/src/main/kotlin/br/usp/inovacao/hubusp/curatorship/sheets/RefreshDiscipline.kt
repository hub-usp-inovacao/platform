package br.usp.inovacao.hubusp.curatorship.sheets

import br.usp.inovacao.hubusp.curatorship.Mailer

class RefreshDiscipline(
    val mailer: Mailer,
    val spreadsheetReader: SpreadsheetReader,
    val disciplineRepository: DisciplineRepository,
    val disciplineErrorRepository: DisciplineErrorRepository
){
    companion object {
        private const val INDEX_CORRECTION_FACTOR = 2
        private val errorsList = mutableListOf<DisciplineValidationError>()
    }

    private fun validateRow(rowIndex: Int, row: List<String?>) = try {
        Discipline.fromRow(row)
    } catch (e: ValidationException) {
        DisciplineValidationError(
            errors = e.messages,
            spreadsheetLineNumber = rowIndex + INDEX_CORRECTION_FACTOR
        )
    }

    private fun persistValidData(data: Any) = when(data) {
        is Discipline -> try {
            disciplineRepository.save(data)
        } catch (e: UniquenessException) {
            disciplineErrorRepository.save(DisciplineUniquenessError(error = e.message))
        }
        is DisciplineValidationError -> {
            disciplineErrorRepository.save(data)
            errorsList.add(data)
        }
        else -> throw RuntimeException("Error while persisting Discipline: data isn't Discipline nor DisciplineValidationError/DisciplineUniquenessError")
    }

    fun refresh() {
        try {
            val data = spreadsheetReader
                .read(Sheets.Disciplines)
                .drop(1)
                .mapIndexed(this::validateRow)
            if(data.filterIsInstance<Discipline>().isNotEmpty()){
                disciplineRepository.clean()
                disciplineErrorRepository.clean()
                data.forEach(this::persistValidData)
                if(errorsList.isNotEmpty()){
                    val errorMail = errorsList.joinToString("\n") { validationError ->
                        val formattedErrors = validationError.errors.map { error ->
                            val property = error.substringBefore(": ")
                            val message = error.substringAfter(": ")
                            val column = Discipline.propertyToColumn[property]

                            if (column != null) {
                                "Coluna $column ($property): $message"
                            } else {
                                error
                            }
                        }.joinToString(", ")
                        "Erros na linha ${validationError.spreadsheetLineNumber}: $formattedErrors"
                    }
                    mailer.notifySpreadsheetError("Foram encontrados erros na planilha de Disciplinas:\n\n${errorMail}", "Relatório Semanal - Disciplinas")
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
