package br.usp.inovacao.hubusp.curatorship.sheets

import br.usp.inovacao.hubusp.curatorship.Mailer

class RefreshInitiative(
    val mailer: Mailer,
    val spreadsheetReader: SpreadsheetReader,
    val initiativeRepository: InitiativeRepository,
    val initiativeErrorRepository: InitiativeErrorRepository
) {
    companion object {
        private const val INDEX_CORRECTION_FACTOR = 2
    }

    private fun validateRow(rowIndex: Int, row: List<String?>) = try {
        Initiative.fromRow(row)
    } catch (e: ValidationException) {
        InitiativeValidationError(
            errors = e.messages,
            spreadsheetLineNumber = rowIndex + INDEX_CORRECTION_FACTOR
        )
    }

    private fun persistValidData(data: Any) = when(data) {
        is Initiative -> try {
            initiativeRepository.save(data)
        } catch (e: UniquenessException) {
            initiativeErrorRepository.save(InitiativeUniquenessError(error = e.message))
        }
        is InitiativeValidationError -> initiativeErrorRepository.save(data)
        else -> throw RuntimeException("Error while persisting Initiative: data isn't Initiative nor InitiativeValidationError/InitiativeUniquenessError")
    }

    fun refresh() {
        try {
            // TODO: delete "old" documents, when due
            val data = spreadsheetReader
                .read(Sheets.Initiatives)
                .drop(1)
                .mapIndexed(this::validateRow)
            if(data.filterIsInstance<Initiative>().isNotEmpty()){
                initiativeRepository.clean()
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
