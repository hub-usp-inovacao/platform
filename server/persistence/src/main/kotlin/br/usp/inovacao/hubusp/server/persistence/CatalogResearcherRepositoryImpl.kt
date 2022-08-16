package br.usp.inovacao.hubusp.server.persistence

import br.usp.inovacao.hubusp.server.catalog.Researcher
import br.usp.inovacao.hubusp.server.catalog.ResearcherRepository
import br.usp.inovacao.hubusp.server.catalog.ResearcherSearchParams
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import org.litote.kmongo.find
import org.litote.kmongo.getCollection

class CatalogResearcherRepositoryImpl(
    db: MongoDatabase
) : ResearcherRepository {
    companion object {
        const val collectionName = "skills"
    }

    private val researcherCollection: MongoCollection<Researcher>

    init {
        researcherCollection = db.getCollection<Researcher>(collectionName)
    }

    override fun filter(params: ResearcherSearchParams): Set<Researcher> {
        val filter = params.toCollectionFilter()

        return researcherCollection
            .find(filter)
            .toSet()
    }
}
