package br.usp.inovacao.hubusp.server.persistence.curatorship

import br.usp.inovacao.hubusp.curatorship.sheets.Researcher
import br.usp.inovacao.hubusp.curatorship.sheets.ResearcherRepository
import br.usp.inovacao.hubusp.curatorship.sheets.UniquenessException
import br.usp.inovacao.hubusp.server.persistence.models.ResearcherModel
import br.usp.inovacao.hubusp.server.persistence.models.KnowledgeAreasModel
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters
import com.mongodb.MongoWriteException
import org.litote.kmongo.getCollection
import java.time.LocalDateTime
import java.time.LocalDate

class ResearcherRepositoryImpl(
    db: MongoDatabase
) : ResearcherRepository {
    private val researcherCollection : MongoCollection<ResearcherModel>

    init {
        researcherCollection = db.getCollection<ResearcherModel>("skills")
    }

    override fun save(researcher: Researcher) {

        val knowledgeAreasModel = KnowledgeAreasModel(
            major = researcher.area.major ?: emptySet(),
            minors = researcher.area.minors ?: emptySet(),
        )

        val researcherModel = ResearcherModel(
            name = researcher.name ?: "",
            email = researcher.email ?: "",
            unities = researcher.unities ?: emptySet(),
            keywords = researcher.keywords ?: emptySet(),
            lattes = researcher.lattes ?: "",
            photo = researcher.photo ?: "",
            skills = researcher.skills ?: emptySet(),
            services = researcher.services ?: emptySet(),
            equipments = researcher.equipments ?: emptySet(),
            phone = researcher.phone ?: "",
            limitDate = researcher.limitDate ?: "",
            bond = researcher.bond ?: "",
            campus = researcher.campus ?: "",
            area = knowledgeAreasModel
        )

        try {
            researcherCollection.insertOne(researcherModel)
        } catch (e: MongoWriteException) {
            when (e.getCode()) {
                11000 -> throw UniquenessException("Researcher with name ${researcher.name} already exists")
                else -> throw e
            }
        }
    }
    override fun clean() {
        researcherCollection.deleteMany(Filters.empty())
    }
}
