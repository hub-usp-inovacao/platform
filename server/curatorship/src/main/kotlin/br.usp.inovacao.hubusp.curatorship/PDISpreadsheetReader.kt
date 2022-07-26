package br.usp.inovacao.hubusp.curatorship

interface PDISpreadsheetReader {
    fun fetch(): List<List<String>>
}