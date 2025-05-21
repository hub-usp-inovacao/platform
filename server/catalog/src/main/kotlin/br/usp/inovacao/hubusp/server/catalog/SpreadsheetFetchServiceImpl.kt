package br.usp.inovacao.hubusp.server.catalog

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class SpreadsheetFetchServiceImpl(
    private val disciplineRepository: DisciplineRepository,
    private val companyRepository: CompanyRepository,
    private val patentRepository: PatentRepository,
    private val initiativeRepository: InitiativeRepository,
    private val researcherRepository: ResearcherRepository
) : SpreadsheetFetchService {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun fetchAllWithReport() {
        logger.info("[fetch|${java.time.LocalDateTime.now()}] task running!")
        // Aqui você implementaria a lógica de fetch das planilhas para cada entidade
        // Exemplo fictício:
        // disciplineRepository.fetchFromSpreadsheet(withReport = true)
        // companyRepository.fetchFromSpreadsheet(withReport = true)
        // etc.
        logger.info("[fetch|${java.time.LocalDateTime.now()}] task ran!")
    }
}