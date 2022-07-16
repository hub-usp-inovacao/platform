package br.usp.inovacao.hubusp.server.persistence

import br.usp.inovacao.hubusp.server.catalog.Researcher
import br.usp.inovacao.hubusp.server.catalog.ResearcherRepository
import br.usp.inovacao.hubusp.server.catalog.ResearcherSearchParams
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import org.litote.kmongo.find
import org.litote.kmongo.getCollection

class CatalogReseacherRepositoryImpl(
    db: MongoDatabase
) : ResearcherRepository {
    companion object {
        const val collectionName = "skills"
    }

    private val reseacherCollection: MongoCollection<Researcher>

    init {
        reseacherCollection = db.getCollection<Researcher>(collectionName)
    }

    override fun filter(params: ResearcherSearchParams): Set<Researcher> {
        val filter = params.toCollectionFilter()

        return reseacherCollection
            .find(filter)
            .toSet()
    }
}
