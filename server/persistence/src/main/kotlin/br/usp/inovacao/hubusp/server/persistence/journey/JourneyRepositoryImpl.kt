package br.usp.inovacao.hubusp.server.persistence.journey

import br.usp.inovacao.hubusp.server.discovery.*
import br.usp.inovacao.hubusp.server.persistence.models.DisciplineModel
import br.usp.inovacao.hubusp.server.persistence.models.InitiativeModel
import br.usp.inovacao.hubusp.server.persistence.models.PDIModel
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import org.litote.kmongo.find
import org.litote.kmongo.getCollection

class JourneyRepositoryImpl(
    db: MongoDatabase
) : JourneyRepository {

    private var disciplineCollection: MongoCollection<DisciplineModel>
    private var initiativeCollection: MongoCollection<InitiativeModel>
    private var pdiCollection: MongoCollection<PDIModel>

    init {
        disciplineCollection = db.getCollection<DisciplineModel>("disciplines")
        initiativeCollection = db.getCollection<InitiativeModel>("iniciatives")
        pdiCollection = db.getCollection<PDIModel>("pdis")
    }

    override fun find(step: JourneyStep, filter: Filter) = when(step) {
        JourneyStep.Learn -> findLearn(filter as LearnStepFilters)
        JourneyStep.Practice -> findPractice(filter as PracticeStepFilter)
        JourneyStep.Create -> findCreate(filter as CreateStepFilter)
        JourneyStep.Improve -> findImprove(filter as ImproveStepFilter)
        JourneyStep.Fund -> findFund(filter as FundStepFilter)
    }

    private fun findLearn(filter: LearnStepFilters): Set<JourneyRecord> {
        return disciplineCollection
            .find(filter.toCollectionFilter())
            .map {
                JourneyRecord(
                    name = it.name,
                    url = it.url
                )
            }
            .toSet()
    }

    private fun findPractice(filter: PracticeStepFilter): Set<JourneyRecord> {
        return initiativeCollection
            .find(filter.toCollectionFilter())
            .map {
                JourneyRecord(
                    name = it.name,
                    url = it.url
                )
            }
            .toSet()
    }

    private fun findCreate(filter: CreateStepFilter): Set<JourneyRecord> {
        // TODO: fetch incubs from the dedicated spreadsheet
        // https://docs.google.com/spreadsheets/d/1kS55eqf_xEfOExh1aEGSvjTFDbtCRWHYSEyh4f31nww/edit#gid=591115329
        return emptySet()
    }

    private fun findImprove(filter: ImproveStepFilter): Set<JourneyRecord> {
        return pdiCollection
            .find(filter.toCollectionFilter())
            .map {
                JourneyRecord(
                    name = it.name,
                    url = it.site
                )
            }
            .toSet()
    }

    private fun findFund(filter: Filter): Set<JourneyRecord> {
        // TODO: fetch funds from the dedicated spreadsheet
        // https://docs.google.com/spreadsheets/d/12O4u6P5Ytf9_uXmHu89vC_7oE_orw7GDKRajzSzLh5g/edit#gid=0
        return emptySet()
    }
}

fun LearnStepFilters.toCollectionFilter(): String {
    val inner = mutableSetOf<String>()

    if (level != null) inner.add("level:\"$level\"")

    if (nature != null) inner.add("nature:\"$nature\"")

    return "{" +  inner.joinToString(",") + "}"
}

fun PracticeStepFilter.toCollectionFilter(): String {
    val inner = mutableSetOf<String>()

    if (category != null) inner.add("classification:\"$category\"")

    return "{" + inner.joinToString(",") + "}"
}

fun ImproveStepFilter.toCollectionFilter(): String {
    val inner = mutableSetOf<String>()

    if (category != null) inner.add("category:\"$category\"")

    return "{" + inner.joinToString(",") + "}"
}