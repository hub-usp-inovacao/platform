package br.usp.inovacao.hubusp.server.curatorship.spreadsheets

import br.usp.inovacao.hubusp.server.curatorship.ValidationReport

interface ValidationReportRepository<T> {
    fun save(report: ValidationReport<T>)
}
