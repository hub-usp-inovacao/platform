package br.usp.inovacao.hubusp.server.persistence

import br.usp.inovacao.hubusp.server.curatorship.ValidationReport
import br.usp.inovacao.hubusp.server.curatorship.spreadsheets.ValidationReportRepository

class ValidationReportRepositoryImpl<T> : ValidationReportRepository<T> {
    private val reports = mutableListOf<ValidationReport<T>>()

    override fun save(report: ValidationReport<T>) {
        reports.add(report)
    }
}
