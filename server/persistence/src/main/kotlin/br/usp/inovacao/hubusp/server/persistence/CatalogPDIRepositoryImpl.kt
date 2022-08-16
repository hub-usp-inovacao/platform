package br.usp.inovacao.hubusp.server.persistence

import br.usp.inovacao.hubusp.server.catalog.PDI
import br.usp.inovacao.hubusp.server.catalog.PDIRepository
import br.usp.inovacao.hubusp.server.catalog.PDISearchParams
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import org.litote.kmongo.find
import org.litote.kmongo.getCollection

class CatalogPDIRepositoryImpl(
    db: MongoDatabase
) : PDIRepository {

    private val pdiCollection: MongoCollection<PDI>

    init {
        pdiCollection = db.getCollection<PDI>("pdis")
    }

    override fun filter(params: PDISearchParams): Set<PDI> {
        val filter: String = params.toCollectionFilter()

        return pdiCollection.find(filter).toSet()

    }
}