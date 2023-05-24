package br.usp.inovacao.hubusp.server.persistence.curatorship

import br.usp.inovacao.hubusp.curatorship.sheets.Discipline
import br.usp.inovacao.hubusp.curatorship.sheets.DisciplineRepository
import br.usp.inovacao.hubusp.server.persistence.models.DisciplineCategory
import br.usp.inovacao.hubusp.server.persistence.models.DisciplineDescription
import br.usp.inovacao.hubusp.server.persistence.models.DisciplineModel
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import org.litote.kmongo.getCollection

class DisciplineRepositoryImpl (
    db: MongoDatabase
) : DisciplineRepository{

    private val disciplineCollection : MongoCollection<DisciplineModel>

    init {
        disciplineCollection = db.getCollection<DisciplineModel>("disciplines")
    }

    override fun save(discipline: Discipline) {

        val (innovation, business, entrepreneurship, intellectual_property) = discipline.category!!
        val (long, short) = discipline.description!!

        val category = DisciplineCategory(
            innovation,
            business,
            entrepreneurship,
            intellectual_property
        )

        val description = DisciplineDescription(
            long ?: "",
            short ?: ""
        )

        val disciplineModel = DisciplineModel(
            campus = discipline.campus!!,
            category = category,
            description = description,
            keywords = emptySet(),
            level = discipline.level!!,
            name = discipline.name!!,
            nature = discipline.nature!!,
            offeringPeriod = discipline.offeringPeriod!!,
            start_date = discipline.start_date ?: "",
            unity = discipline.unity!!,
            url = discipline.url ?: ""
        )

        disciplineCollection.insertOne(disciplineModel)
    }

}