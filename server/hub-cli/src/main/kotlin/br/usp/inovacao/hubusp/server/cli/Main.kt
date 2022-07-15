package br.usp.inovacao.hubusp.server.cli

import br.usp.inovacao.hubusp.server.curatorship.CurateDisciplines
import br.usp.inovacao.hubusp.server.curatorship.spreadsheets.SpreadsheetsAdapterImpl
import br.usp.inovacao.hubusp.server.persistence.CatalogDisciplineRepositoryImpl
import br.usp.inovacao.hubusp.server.persistence.CuratorshipDisciplineRepositoryImpl
import br.usp.inovacao.hubusp.server.persistence.ValidationReportRepositoryImpl
import br.usp.inovacao.hubusp.server.persistence.configureDB
import com.github.ajalt.clikt.core.subcommands

fun main(args: Array<String>) {
    val apiKey = ConfigLoader.SHEETS_API_KEY

    val db = configureDB(
        ConfigLoader.DATASOURCE_PROTOCOL,
        ConfigLoader.DATASOURCE_HOST,
        ConfigLoader.DATASOURCE_PORT,
        ConfigLoader.DATASOURCE_DBNAME
    )

    val disciplineRepository = CatalogDisciplineRepositoryImpl(db)

    val curateDisciplines = CurateDisciplines(
        disciplineRepository = CuratorshipDisciplineRepositoryImpl(),
        reportRepository = ValidationReportRepositoryImpl(),
        sheetsAdapter = SpreadsheetsAdapterImpl(apiKey)
    )

    CLI()
        .subcommands(
            LogDisciplines(disciplineRepository),
            CuratorRefreshDisciplines(curateDisciplines)
        )
        .main(args)
}
