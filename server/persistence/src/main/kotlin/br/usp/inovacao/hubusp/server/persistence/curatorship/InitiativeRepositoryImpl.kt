package br.usp.inovacao.hubusp.server.persistence.curatorship

import br.usp.inovacao.hubusp.curatorship.sheets.Initiative
import br.usp.inovacao.hubusp.curatorship.sheets.InitiativeRepository
import br.usp.inovacao.hubusp.curatorship.sheets.UniquenessException
import br.usp.inovacao.hubusp.server.persistence.models.InitiativeModel
import br.usp.inovacao.hubusp.server.persistence.models.InitiativeContact
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters
import com.mongodb.MongoWriteException
import org.litote.kmongo.getCollection
import java.time.LocalDateTime

class InitiativeRepositoryImpl(
    db: MongoDatabase
) : InitiativeRepository {
    private val initiativeCollection: MongoCollection<InitiativeModel>

    init {
        initiativeCollection = db.getCollection<InitiativeModel>("initiatives")
    }

    override fun save(initiative: Initiative) {
        val contactModel = InitiativeContact(
            person = initiative.contact.person ?: "",
            info = initiative.contact.info ?: ""
        )

        val initiativeModel = InitiativeModel(
            name = initiative.name ?: "",
            classification = initiative.classification ?: "",
            localization = initiative.localization ?: "",
            unity = initiative.unity ?: "",
            tags = initiative.tags ?: emptySet(),
            url = initiative.url ?: "",
            description = initiative.description ?: "",
            email = initiative.email ?: emptySet(),
            contact = contactModel
        )

        try {
            initiativeCollection.insertOne(initiativeModel)
        } catch (e: MongoWriteException) {
            when (e.code) {
                11000 -> throw UniquenessException("Initiative with name ${initiative.name} already exists")
                else -> throw e
            }
        }
    }

    override fun clean() {
        val currentTime = LocalDateTime.now()
        val thirtySecondsAgo = currentTime.minusSeconds(30)
        val filter = Filters.lt("timestamp", thirtySecondsAgo)
        initiativeCollection.deleteMany(filter)
    }
}
