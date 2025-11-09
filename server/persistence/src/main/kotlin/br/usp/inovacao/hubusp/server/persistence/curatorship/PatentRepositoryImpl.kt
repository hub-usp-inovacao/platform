package br.usp.inovacao.hubusp.server.persistence.curatorship

import br.usp.inovacao.hubusp.curatorship.sheets.Patent
import br.usp.inovacao.hubusp.curatorship.sheets.PatentRepository
import br.usp.inovacao.hubusp.curatorship.sheets.UniquenessException
import br.usp.inovacao.hubusp.server.persistence.models.PatentModel
import br.usp.inovacao.hubusp.server.persistence.models.PatentAreaModel
import br.usp.inovacao.hubusp.server.persistence.models.PatentDualClassificationModel
import com.mongodb.MongoWriteException
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters
import org.litote.kmongo.getCollection

class PatentRepositoryImpl(
    db: MongoDatabase
) : PatentRepository {
    private val patentCollection: MongoCollection<PatentModel>

    init {
        patentCollection = db.getCollection<PatentModel>("patents")
    }

    override fun save(patent: Patent) {
        val patentAreaModel =
            PatentAreaModel(
                cip = patent.classification.primary.cip ?: "",
                subarea = patent.classification.primary.subarea ?: "",
            )

        val patentDualClassificationModel =
            PatentDualClassificationModel(
                primary = patentAreaModel,
                secondary =
                    patent.classification.secondary?.let {
                        PatentAreaModel(
                            cip = it.cip ?: "",
                            subarea = it.subarea ?: "",
                        )
                    },
            )

        val patentModel =
            PatentModel(
                classification = patentDualClassificationModel,
                countries_with_protection = patent.countries_with_protection ?: emptySet(),
                inventors = patent.inventors ?: emptySet(),
                ipcs = patent.ipcs ?: emptySet(),
                name = patent.name ?: "",
                owners = patent.owners ?: emptySet(),
                photo = patent.photo,
                status = patent.status ?: "",
                summary = patent.summary ?: "",
                url = patent.url,
            )

        try {
            patentCollection.insertOne(patentModel)
        } catch (e: MongoWriteException) {
            when (e.getCode()) {
                11000 ->
                    throw UniquenessException(
                        "Patent with name ${patent.name} already exists",
                    )
                else -> throw e
            }
        }
    }

    override fun clean() {
        patentCollection.deleteMany(Filters.empty())
    }
}