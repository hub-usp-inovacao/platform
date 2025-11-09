package br.usp.inovacao.hubusp.server.persistence.models

@kotlinx.serialization.Serializable
class PatentValidationErrorModel(val errors: List<String>, val spreadsheetLineNumber: Int)