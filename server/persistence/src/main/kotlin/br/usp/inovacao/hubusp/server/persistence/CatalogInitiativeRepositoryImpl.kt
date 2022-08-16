package br.usp.inovacao.hubusp.server.persistence

import br.usp.inovacao.hubusp.server.catalog.Initiative
import br.usp.inovacao.hubusp.server.catalog.InitiativeRepository
import br.usp.inovacao.hubusp.server.catalog.InitiativeSearchParams
import com.mongodb.client.MongoDatabase
import org.litote.kmongo.find
import org.litote.kmongo.getCollection

class CatalogInitiativeRepositoryImpl(
    db: MongoDatabase
): InitiativeRepository {
    private val initiativeCollection = db.getCollection<Initiative>("iniciatives")

    override fun filter(params: InitiativeSearchParams): Set<Initiative> {
        val filter = params.toCollectionFilter()

        return initiativeCollection.find(filter).toSet()
    }
}