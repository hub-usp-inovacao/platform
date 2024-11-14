package br.usp.inovacao.hubusp.server.persistence.curatorship

import br.usp.inovacao.hubusp.curatorship.sheets.DisciplineErrorRepository
import br.usp.inovacao.hubusp.curatorship.sheets.DisciplineValidationError
import br.usp.inovacao.hubusp.curatorship.sheets.DisciplineUniquenessError
import br.usp.inovacao.hubusp.server.persistence.models.DisciplineValidationErrorModel
import br.usp.inovacao.hubusp.server.persistence.models.DisciplineUniquenessErrorModel
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import org.litote.kmongo.getCollection

class DisciplineErrorRepositoryImpl(
    db : MongoDatabase
) : DisciplineErrorRepository {
    private val disciplineValidationErrorCollection : MongoCollection<DisciplineValidationErrorModel>
    private val disciplineUniquenessErrorCollection : MongoCollection<DisciplineUniquenessErrorModel>

    init {
        disciplineValidationErrorCollection = db.getCollection<DisciplineValidationErrorModel>("discipline_errors")
        disciplineUniquenessErrorCollection = db.getCollection<DisciplineUniquenessErrorModel>("discipline_errors")
    }

    override fun save(disciplineError: Any) {
        val disciplineErrorModel = when (disciplineError) {
            is DisciplineValidationError -> DisciplineValidationErrorModel(
                errors = disciplineError.errors,
                spreadsheetLineNumber = disciplineError.spreadsheetLineNumber,
                delivered = disciplineError.delivered
            )
            is DisciplineUniquenessError -> DisciplineUniquenessErrorModel(
                error = disciplineError.error,
                delivered = disciplineError.delivered
            )
            else -> throw IllegalArgumentException("Invalid discipline error type")
        }

        when (disciplineError) {
            is DisciplineValidationError -> disciplineValidationErrorCollection.insertOne(disciplineErrorModel as DisciplineValidationErrorModel)
            is DisciplineUniquenessError -> disciplineUniquenessErrorCollection.insertOne(disciplineErrorModel as DisciplineUniquenessErrorModel)
            else -> throw IllegalArgumentException("Invalid discipline error type")
        }
    }
}
