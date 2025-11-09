package br.usp.inovacao.hubusp.curatorship.sheets

class PatentValidationError(val errors: List<String>, val spreadsheetLineNumber: Int)