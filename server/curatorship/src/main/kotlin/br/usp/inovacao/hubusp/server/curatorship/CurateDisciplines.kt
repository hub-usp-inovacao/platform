package br.usp.inovacao.hubusp.server.curatorship

import br.usp.inovacao.hubusp.server.curatorship.spreadsheets.SpreadsheetsAdapter
import br.usp.inovacao.hubusp.server.curatorship.spreadsheets.ValidationReportRepository

class CurateDisciplines(
    private val disciplineRepository: DisciplineRepository,
    private val reportRepository: ValidationReportRepository<Discipline>,
    private val sheetsAdapter: SpreadsheetsAdapter,
    private val validator: DisciplineValidator = DisciplineValidator()
) {
    fun refreshDisciplines() {
        val disciplinesByApproval = sheetsAdapter
            .fetchDisciplines()
            .map { validator.validate(it) }
            .groupBy { it.approved }

        disciplinesByApproval[true]
            ?.map { it.entity }
            ?.let { disciplineRepository.batchRefresh(it) }

        disciplinesByApproval[false]
            ?.forEach { reportRepository.save(it) }
    }
}
