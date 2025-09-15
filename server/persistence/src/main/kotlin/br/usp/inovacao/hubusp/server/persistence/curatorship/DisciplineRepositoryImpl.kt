package br.usp.inovacao.hubusp.server.persistence.curatorship

import br.usp.inovacao.hubusp.curatorship.sheets.Discipline
import br.usp.inovacao.hubusp.curatorship.sheets.DisciplineRepository
import br.usp.inovacao.hubusp.curatorship.sheets.UniquenessException
import br.usp.inovacao.hubusp.server.persistence.models.DisciplineCategory
import br.usp.inovacao.hubusp.server.persistence.models.DisciplineModel
import br.usp.inovacao.hubusp.server.persistence.models.DisciplineOffering
import com.mongodb.MongoWriteException
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters
import org.litote.kmongo.getCollection

class DisciplineRepositoryImpl(db: MongoDatabase) : DisciplineRepository {
    private val disciplineCollection: MongoCollection<DisciplineModel>

    init {
        disciplineCollection = db.getCollection<DisciplineModel>("disciplines")
    }

    override fun save(discipline: Discipline) {
        val disciplineCategory =
            DisciplineCategory(
                innovation = discipline.category.innovation ?: false,
                business = discipline.category.business ?: false,
                entrepreneurship = discipline.category.entrepreneurship ?: false,
                intellectual_property = discipline.category.intellectual_property ?: false,
            )

        val disciplineOfferings =
            discipline.offerings
                .map {
                    DisciplineOffering(
                        classCode = it.classCode ?: "",
                        startDate = it.startDate ?: "",
                        endDate = it.endDate ?: "",
                        professors = it.professors,
                    )
                }
                .toSet()

        val disciplineModel =
            DisciplineModel(
                campus = discipline.campus ?: "",
                category = disciplineCategory,
                description = discipline.description ?: "",
                keywords = discipline.keywords ?: emptySet(),
                level = discipline.level ?: "",
                name = discipline.name ?: "",
                nature = discipline.nature ?: "",
                offeringPeriod = discipline.offeringPeriod ?: "",
                start_date = discipline.start_date ?: "",
                unity = discipline.unity ?: "",
                url = discipline.url ?: "",
                beingOffered = discipline.beingOffered,
                offerings = disciplineOfferings,
            )

        try {
            disciplineCollection.insertOne(disciplineModel)
        } catch (e: MongoWriteException) {
            when (e.getCode()) {
                11000 ->
                    throw UniquenessException(
                        "Discipline with name ${discipline.name} already exists",
                    )
                else -> throw e
            }
        }
    }

    override fun clean() {
        disciplineCollection.deleteMany(Filters.empty())
    }
}
