package br.usp.inovacao.hubusp.curatorship.sheets

import br.usp.inovacao.hubusp.curatorship.Mailer

class RefreshPDI(
    val mailer: Mailer,
    val spreadsheetReader: SpreadsheetReader
) {
    fun refresh() {
        // fetch data from spreadsheet
        // if exception, suspend and send email
        // validate each data
        // if it valid, save PDI
        // else, save a PDIValidationError
        try {
            spreadsheetReader.fakeRead(Sheets.PDIs)
        } catch (e: SheetReadingException) {
            mailer.notifySpreadsheetError(e.message)
        }
    }
}