package br.usp.inovacao.hubusp.curatorship.sheets

class SheetReadingException(
    sheetName: String,
    sheetId: String,
    override val message: String,
): RuntimeException(
    "$message - $sheetName@$sheetId"
)