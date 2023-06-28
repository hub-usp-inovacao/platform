package br.usp.inovacao.hubusp.server.persistence

import br.usp.inovacao.hubusp.server.catalog.PDI
import br.usp.inovacao.hubusp.server.catalog.PDIRepository
import br.usp.inovacao.hubusp.server.catalog.PDISearchParams
import br.usp.inovacao.hubusp.server.persistence.models.PDIModel
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import org.litote.kmongo.find
import org.litote.kmongo.getCollection

fun PDIModel.toCatalogPDI(): PDI = PDI(
    category, name, campus, unity, coordinator,
    site, email, phone, description, tags, timestamp,
)

class CatalogPDIRepositoryImpl(
    db: MongoDatabase
) : PDIRepository {

    private val pdiCollection: MongoCollection<PDIModel>

    init {
        pdiCollection = db.getCollection<PDIModel>("pdis")
    }

    override fun filter(params: PDISearchParams): Set<PDI> {
        val filter: String = params.toCollectionFilter()

        return pdiCollection
            .find(filter)
            .map { it.toCatalogPDI() }
            .toSet()

    }
}