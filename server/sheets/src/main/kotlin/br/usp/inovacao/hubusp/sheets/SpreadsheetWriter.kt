package br.usp.inovacao.hubusp.sheets

import java.io.IOException

class SpreadsheetWriter(val sheetId: String, val sheetName: String) {
    /**
     * Append rows to google sheets table
     *
     * See examples of tableRange here:
     * https://developers.google.com/workspace/sheets/api/guides/values#append_values
     */
    fun append(matrix: Matrix<String?>, tableRange: String = "A1"): String {
        try {
            val separator = if (tableRange == "") "" else "!"
            val range = "'${sheetName}'${separator}${tableRange}"
            return SpreadsheetService.append(sheetId, range, matrix).updates.updatedRange
        } catch (e: IOException) {
            throw SpreasheetException(
                sheetName,
                sheetId,
                message = "Failed to connect to spreadsheet: ${e.message}",
            )
        } catch (e: Exception) {
            throw SpreasheetException(
                sheetName,
                sheetId,
                message = "Unexpected response from spreadsheet: ${e.message}",
            )
        }
    }
}
