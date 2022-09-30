package br.usp.inovacao.hubusp.server.persistence.curatorship

import br.usp.inovacao.hubusp.curatorship.sheets.PDI
import br.usp.inovacao.hubusp.curatorship.sheets.PDIRepository
import br.usp.inovacao.hubusp.server.persistence.models.PDIModel
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import org.litote.kmongo.deleteMany
import org.litote.kmongo.getCollection

class PDIRepositoryImpl(
    db: MongoDatabase
) : PDIRepository {
    private val pdiCollection : MongoCollection<PDIModel>

    init {
        pdiCollection = db.getCollection<PDIModel>("pdis")
    }

    override fun save(pdi: PDI) {
        // TODO: test
        val pdiModel = PDIModel(
           category = pdi.category!!,
           name = pdi.name!!,
           campus = pdi.campus!!,
           unity = pdi.unity!!,
           description = pdi.description!!,
           tags = pdi.keywords!!,

           coordinator = pdi.coordinator,
           site = pdi.site,
           email = pdi.email,
           phone = pdi.phone,
        )

        pdiCollection.insertOne(pdiModel)
    }

    override fun deleteAll() {
        val matchesAllDocuments = """{}"""
        pdiCollection.deleteMany(matchesAllDocuments)
    }
}