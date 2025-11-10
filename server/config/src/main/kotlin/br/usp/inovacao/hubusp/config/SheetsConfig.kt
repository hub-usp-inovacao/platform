package br.usp.inovacao.hubusp.config

data class SheetsConfig(
    val apiKey: String,
    val company: SpreadsheetConfig,
    val discipline: SpreadsheetConfig,
    val funds: SpreadsheetConfig,
    val incubators: SpreadsheetConfig,
    val initiative: SpreadsheetConfig,
    val patent: SpreadsheetConfig,
    val pdi: SpreadsheetConfig,
    val researcher: SpreadsheetConfig,
    var companyRegisterForm: SpreadsheetConfig,
    var companyUpdateForm: SpreadsheetConfig,
)

data class SpreadsheetConfig(var id: String, var tab: String)
