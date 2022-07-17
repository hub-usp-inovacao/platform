package br.usp.inovacao.hubusp.server.persistence

import br.usp.inovacao.hubusp.server.catalog.Patent
import br.usp.inovacao.hubusp.server.catalog.PatentRepository
import br.usp.inovacao.hubusp.server.catalog.PatentSearchParams
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import org.litote.kmongo.find
import org.litote.kmongo.getCollection

class CatalogPatentRepositoryImpl(
    db: MongoDatabase
) : PatentRepository {
    private val patentCollection: MongoCollection<Patent>

    init {
        patentCollection = db.getCollection<Patent>("patents")
    }

    override fun filter(params: PatentSearchParams): Set<Patent> {
        val filter = params.toCollectionFilter()

        return patentCollection.find(filter).toSet()
    }
}