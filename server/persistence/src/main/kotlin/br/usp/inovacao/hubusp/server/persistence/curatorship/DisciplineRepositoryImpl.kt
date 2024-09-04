package br.usp.inovacao.hubusp.server.persistence.curatorship

import br.usp.inovacao.hubusp.curatorship.sheets.Discipline
import br.usp.inovacao.hubusp.curatorship.sheets.DisciplineRepository
import br.usp.inovacao.hubusp.server.persistence.models.DisciplineModel
import br.usp.inovacao.hubusp.server.persistence.models.DisciplineCategory
import br.usp.inovacao.hubusp.server.persistence.models.DisciplineDescription
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters
import org.litote.kmongo.getCollection
import java.time.LocalDateTime

class DisciplineRepositoryImpl(
    db: MongoDatabase
) : DisciplineRepository {
    private val disciplineCollection : MongoCollection<DisciplineModel>

    init {
        disciplineCollection = db.getCollection<DisciplineModel>("disciplines")
    }

    override fun save(discipline: Discipline) {
        // TODO: test
        // TODO: improve CompanyModel to follow validation structure (avoid parsing null)

        val disciplineDescription = DisciplineDescription(
            long = discipline.description.long ?: "",
            short = discipline.description.short ?: ""
        )

        val disciplineCategory = DisciplineCategory(
            innovation = discipline.category.innovation ?: false,
            business = discipline.category.business ?: false,
            entrepreneurship = discipline.category.entrepreneurship ?: false,
            intellectual_property = discipline.category.intellectual_property ?: false
        )

        val disciplineModel = DisciplineModel(
            campus = discipline.campus ?: "",
            category = disciplineCategory,
            description = disciplineDescription,
            keywords = discipline.keywords ?: emptySet(),
            level = discipline.level ?: "",
            name = discipline.name ?: "",
            nature = discipline.nature ?: "",
            offeringPeriod = discipline.offeringPeriod ?: "",
            start_date = discipline.start_date ?: "",
            unity = discipline.unity ?: "",
            url = discipline.url ?: ""
        )

        disciplineCollection.insertOne(disciplineModel)
    }

        override fun clean() {
            val currentTime = LocalDateTime.now()

            val fiveSecondsAgo = currentTime.minusSeconds(30)

            val filter = Filters.lt("timestamp", fiveSecondsAgo)

            disciplineCollection.deleteMany(filter)
        }
    }