package br.usp.inovacao.hubusp.curatorship.sheets

import br.usp.inovacao.hubusp.curatorship.Mailer

class RefreshDiscipline(
    val mailer: Mailer,
    val spreadsheetReader: SpreadsheetReader,
    val disciplineRepository: DisciplineRepository,
    val disciplineErrorRepository: DisciplineErrorRepository
) {

    private fun validateRow(rowIndex: Int, row: List<String?>) = try {
        Discipline.fromRow(row)
    } catch (e: ValidationException) {
        DisciplineValidationError(
            errors = e.messages,
            spreadsheetLineNumber = rowIndex
        )
    }

    private fun persistData(data: Any) = when(data) {
        is Discipline -> disciplineRepository.save(data)
        is DisciplineValidationError -> disciplineErrorRepository.save(data)
        else -> throw RuntimeException("Error while persisting Discipline: data isn't Discipline nor DisciplineValidationError")
    }

    fun refresh() {
        try {
            spreadsheetReader
                .read(Sheets.Disciplines)
                .mapIndexed(this::validateRow)
                .forEach(this::persistData)
        } catch (e: SheetReadingException) {
            mailer.notifySpreadsheetError(e.message)
        }
    }
}