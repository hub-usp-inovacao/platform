package br.usp.inovacao.hubusp.sheets

class SpreasheetException(
    sheetName: String,
    sheetId: String,
    override val message: String,
) : RuntimeException("$message - $sheetName@$sheetId")
