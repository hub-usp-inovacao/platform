package br.usp.inovacao.hubusp.config

data class SheetsConfig(
    var apiKey: String = "",
    var company: SpreadsheetConfig = SpreadsheetConfig(),
    var discipline: SpreadsheetConfig = SpreadsheetConfig(),
    var funds: SpreadsheetConfig = SpreadsheetConfig(),
    var incubators: SpreadsheetConfig = SpreadsheetConfig(),
    var initiative: SpreadsheetConfig = SpreadsheetConfig(),
    var patent: SpreadsheetConfig = SpreadsheetConfig(),
    var pdi: SpreadsheetConfig = SpreadsheetConfig(),
    var researcher: SpreadsheetConfig = SpreadsheetConfig(),
    var companyRegisterForm: SpreadsheetConfig = SpreadsheetConfig(),
)

data class SpreadsheetConfig(var id: String = "", var tab: String = "")
