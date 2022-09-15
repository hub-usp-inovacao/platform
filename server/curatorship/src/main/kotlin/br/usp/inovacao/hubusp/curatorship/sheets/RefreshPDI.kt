package br.usp.inovacao.hubusp.curatorship.sheets

import br.usp.inovacao.hubusp.curatorship.Mailer

class RefreshPDI(
    val mailer: Mailer,
    val spreadsheetReader: SpreadsheetReader,
    val pdiRepository: PDIRepository,
    val pdiErrorRepository: PDIErrorRepository
) {
    private fun persistAfterValidation(data: Matrix<String?>) {
        data.forEachIndexed { listIndex, row ->
            try {
                val pdi = PDI(
                    category = row[0],
                    name = row[1],
                    campus = row[2],
                    unity = row[3],
                    coordinator = row[4],
                    site = row[5],
                    email = row[6],
                    phone = row[7],
                    description = row[8],
                    keywords = row[9]?.split(";")?.toSet()
                )
                pdiRepository.save(pdi)
            } catch (e: ValidationException) {
                val correctionFactor = 2
                val error = PDIValidationError(e.messages,listIndex + correctionFactor)
                pdiErrorRepository.save(error)
            }
        }
    }
    fun refresh() {
        try {
            val data = spreadsheetReader.read(Sheets.PDIs)
            persistAfterValidation(data)
        } catch (e: SheetReadingException) {
            mailer.notifySpreadsheetError(e.message)
        }

    }
}