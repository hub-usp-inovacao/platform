package br.usp.inovacao.hubusp.server.persistence.curatorship

import br.usp.inovacao.hubusp.curatorship.sheets.PatentErrorRepository
import br.usp.inovacao.hubusp.curatorship.sheets.PatentValidationError
import br.usp.inovacao.hubusp.curatorship.sheets.PatentUniquenessError
import br.usp.inovacao.hubusp.server.persistence.models.PatentValidationErrorModel
import br.usp.inovacao.hubusp.server.persistence.models.PatentUniquenessErrorModel
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters
import org.litote.kmongo.getCollection

class PatentErrorRepositoryImpl(
    db : MongoDatabase
) : PatentErrorRepository {
    private val patentValidationErrorCollection : MongoCollection<PatentValidationErrorModel>
    private val patentUniquenessErrorCollection : MongoCollection<PatentUniquenessErrorModel>

    init {
        patentValidationErrorCollection = db.getCollection<PatentValidationErrorModel>("patent_errors")
        patentUniquenessErrorCollection = db.getCollection<PatentUniquenessErrorModel>("patent_errors")
    }

    override fun save(patentError: Any) {
        val patentErrorModel = when (patentError) {
            is PatentValidationError -> PatentValidationErrorModel(
                errors = patentError.errors,
                spreadsheetLineNumber = patentError.spreadsheetLineNumber,
            )
            is PatentUniquenessError -> PatentUniquenessErrorModel(
                error = patentError.error
            )
            else -> throw IllegalArgumentException("Invalid patent error type")
        }

        when (patentError) {
            is PatentValidationError -> patentValidationErrorCollection.insertOne(patentErrorModel as PatentValidationErrorModel)
            is PatentUniquenessError -> patentUniquenessErrorCollection.insertOne(patentErrorModel as PatentUniquenessErrorModel)
            else -> throw IllegalArgumentException("Invalid patent error type")
        }
    }

    override fun clean() {
        patentValidationErrorCollection.deleteMany(Filters.empty())
        patentUniquenessErrorCollection.deleteMany(Filters.empty())
    }
}
