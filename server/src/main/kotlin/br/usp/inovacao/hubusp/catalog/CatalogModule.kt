package br.usp.inovacao.hubusp.catalog

import br.usp.inovacao.hubusp.catalog.application.configureCatalogRouting
import br.usp.inovacao.hubusp.catalog.domain.Discipline
import br.usp.inovacao.hubusp.catalog.domain.DisciplineService
import com.mongodb.client.MongoDatabase
import io.ktor.server.application.Application
import org.litote.kmongo.getCollection

fun Application.configureCatalogModule(db: MongoDatabase) {
    val disciplineCollection = db.getCollection<Discipline>()

    val disciplineService = DisciplineService(disciplineCollection)
    configureCatalogRouting(disciplineService)
}
