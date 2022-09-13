package br.usp.inovacao.hubusp.curatorship.sheets

data class PDIValidationError(val errors:Iterable<String>, val SpreadsheetLineNumber:Int )

