package br.usp.inovacao.hubusp.server.persistence.curatorship

import br.usp.inovacao.hubusp.curatorship.sheets.InitiativeErrorRepository
import br.usp.inovacao.hubusp.curatorship.sheets.InitiativeUniquenessError
import br.usp.inovacao.hubusp.curatorship.sheets.InitiativeValidationError
import br.usp.inovacao.hubusp.server.persistence.models.InitiativeValidationErrorModel
import br.usp.inovacao.hubusp.server.persistence.models.InitiativeUniquenessErrorModel
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import org.litote.kmongo.getCollection

class InitiativeErrorRepositoryImpl(
    db: MongoDatabase
) : InitiativeErrorRepository {

    private val initiativeValidationErrorCollection: MongoCollection<InitiativeValidationErrorModel>
    private val initiativeUniquenessErrorCollection: MongoCollection<InitiativeUniquenessErrorModel>

    init {
        initiativeValidationErrorCollection = db.getCollection<InitiativeValidationErrorModel>("initiative_validation_errors")
        initiativeUniquenessErrorCollection = db.getCollection<InitiativeUniquenessErrorModel>("initiative_uniqueness_errors")
    }

    override fun save(error: InitiativeValidationError) {
        val initiativeErrorModel = InitiativeValidationErrorModel(
            errors = error.errors,
            spreadsheetLineNumber = error.spreadsheetLineNumber
        )
        initiativeValidationErrorCollection.insertOne(initiativeErrorModel)
    }

    override fun save(uniquenessError: InitiativeUniquenessError) {
        val uniquenessErrorModel = InitiativeUniquenessErrorModel(
            error = uniquenessError.error
        )
        initiativeUniquenessErrorCollection.insertOne(uniquenessErrorModel)
    }
}
