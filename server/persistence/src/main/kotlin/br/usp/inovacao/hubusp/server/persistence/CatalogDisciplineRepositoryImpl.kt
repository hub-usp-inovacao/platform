package br.usp.inovacao.hubusp.server.persistence

import br.usp.inovacao.hubusp.server.catalog.Discipline
import br.usp.inovacao.hubusp.server.catalog.DisciplineSearchParams
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import org.litote.kmongo.find
import org.litote.kmongo.getCollection

class CatalogDisciplineRepositoryImpl(
    db: MongoDatabase
) : br.usp.inovacao.hubusp.server.catalog.DisciplineRepository {

    private val collection: MongoCollection<Discipline>

    init {
        collection = db.getCollection<Discipline>("disciplines")
    }

    override fun filter(params: Map<String, List<String>>): Set<Discipline> {
        val filter = DisciplineQueryFilter.from(params).toJson()

        return collection.find(filter).toSet()
    }

    override fun filter(params: DisciplineSearchParams): Set<Discipline> {
        return collection.find(params.toCollectionFilter()).toSet()
    }
}
