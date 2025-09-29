package br.usp.inovacao.hubusp.sheets

import com.google.api.services.sheets.v4.model.AppendValuesResponse
import com.google.api.services.sheets.v4.model.UpdateValuesResponse
import java.io.IOException

class SpreadsheetWriter(val sheetId: String, val sheetName: String) {
    fun append(matrix: Matrix<String?>, startingCell: String = "A1"): String {
        try {
            val range = "'${sheetName}':${startingCell}"
            return SpreadsheetService.append(sheetId, range, matrix).updates.updatedRange
        } catch (_: IOException) {
            throw SpreasheetException(
                sheetName,
                sheetId,
                message = "Failed to connect to spreadsheet",
            )
        } catch (_: Exception) {
            throw SpreasheetException(
                sheetName,
                sheetId,
                message = "Unexpected response from spreadsheet",
            )
        }
    }
}
