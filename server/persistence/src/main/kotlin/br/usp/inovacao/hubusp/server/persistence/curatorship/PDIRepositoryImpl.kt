package br.usp.inovacao.hubusp.server.persistence.curatorship

import br.usp.inovacao.hubusp.curatorship.sheets.PDI
import br.usp.inovacao.hubusp.curatorship.sheets.PDIRepository
import br.usp.inovacao.hubusp.server.persistence.models.PDIModel
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters
import org.litote.kmongo.getCollection
import java.time.LocalDateTime

class PDIRepositoryImpl(
    db: MongoDatabase
) : PDIRepository {
    private val pdiCollection : MongoCollection<PDIModel>

    init {
        pdiCollection = db.getCollection<PDIModel>("pdis")
    }

    override fun save(pdi: PDI) {
        // TODO: test
        // TODO: improve PDIModel to follow validation structure (avoid parsing null)
        val pdiModel = PDIModel(
           category = pdi.category!!,
           name = pdi.name!!,
           campus = pdi.campus!!,
           unity = pdi.unity!!,
           coordinator = pdi.coordinator ?: "",
           site = pdi.site ?: "",
           email = pdi.email ?: "",
           phone = pdi.phone ?: "",
           description = pdi.description!!,
           tags = pdi.keywords!!,
           timestamp = pdi.timestamp!!
        )

        pdiCollection.insertOne(pdiModel)
    }
    override fun clean() {
        val currentTime = LocalDateTime.now()

        val fiveSecondsAgo = currentTime.minusSeconds(5)

        val filter = Filters.lt("timestamp", fiveSecondsAgo)

        pdiCollection.deleteMany(filter)
    }
}