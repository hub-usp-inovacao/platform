package br.usp.inovacao.hubusp.server.persistence.curatorship

import br.usp.inovacao.hubusp.curatorship.sheets.CompanyErrorRepository
import br.usp.inovacao.hubusp.curatorship.sheets.CompanyValidationError
import br.usp.inovacao.hubusp.curatorship.sheets.CompanyUniquenessError
import br.usp.inovacao.hubusp.server.persistence.models.CompanyValidationErrorModel
import br.usp.inovacao.hubusp.server.persistence.models.CompanyUniquenessErrorModel
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters
import org.litote.kmongo.getCollection

class CompanyErrorRepositoryImpl(
    db : MongoDatabase
) : CompanyErrorRepository {
    private val companyValidationErrorCollection : MongoCollection<CompanyValidationErrorModel>
    private val companyUniquenessErrorCollection : MongoCollection<CompanyUniquenessErrorModel>

    init {
        companyValidationErrorCollection = db.getCollection<CompanyValidationErrorModel>("company_errors")
        companyUniquenessErrorCollection = db.getCollection<CompanyUniquenessErrorModel>("company_errors")
    }

    override fun save(companyError: Any) {
        // TODO: test
        val companyErrorModel = when (companyError) {
            is CompanyValidationError -> CompanyValidationErrorModel(
                errors = companyError.errors,
                spreadsheetLineNumber = companyError.spreadsheetLineNumber,
                delivered = companyError.delivered
            )
            is CompanyUniquenessError -> CompanyUniquenessErrorModel(
                error = companyError.error,
                delivered = companyError.delivered
            )
            else -> throw IllegalArgumentException("Invalid company error type")
        }

        when (companyError) {
            is CompanyValidationError -> companyValidationErrorCollection.insertOne(companyErrorModel as CompanyValidationErrorModel)
            is CompanyUniquenessError -> companyUniquenessErrorCollection.insertOne(companyErrorModel as CompanyUniquenessErrorModel)
            else -> throw IllegalArgumentException("Invalid company error type")
        }
    }

    override fun clean() {
        companyValidationErrorCollection.deleteMany(Filters.empty())
        companyUniquenessErrorCollection.deleteMany(Filters.empty())
    }
}
