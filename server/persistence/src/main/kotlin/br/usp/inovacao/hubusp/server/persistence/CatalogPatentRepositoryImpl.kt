package br.usp.inovacao.hubusp.server.persistence

import br.usp.inovacao.hubusp.server.catalog.*
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import kotlinx.serialization.Serializable
import org.litote.kmongo.aggregate
import org.litote.kmongo.find
import org.litote.kmongo.getCollection

@Serializable
data class ClassificationResult(
    val primary: String,
    val secondaries: Set<String>
)

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

    override fun getClassifications(): Set<IPC> = patentCollection
        .aggregate<ClassificationResult>(
            "[{ \$project: {_id:0,classifications:[{primary:\"\$classification.primary.cip\",secondary:\"\$classification.primary.subarea\"},{primary:\"\$classification.secondary.cip\",secondary:\"\$classification.secondary.subarea\"}]} }," +
            "{ \$unwind: {path:\"\$classifications\",preserveNullAndEmptyArrays:false}}," +
            "{ \$project: {primary:\"\$classifications.primary\",secondary:\"\$classifications.secondary\"}}," +
            "{ \$match: {primary:{\$ne: null}, secondary:{\$ne: null}}}," +
            "{ \$group: {_id:\"\$primary\",secondaries:{\$addToSet:\"\$secondary\"}}}," +
            "{ \$project: {_id:0,primary:\"\$_id\",secondaries:1}}]"
        )
        .map { IPC(it.primary, it.secondaries) }
        .toSet()
}