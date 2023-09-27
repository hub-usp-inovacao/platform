package br.usp.inovacao.hubusp.server.persistence.curatorship

import br.usp.inovacao.hubusp.curatorship.sheets.CompanyErrorRepository
import br.usp.inovacao.hubusp.curatorship.sheets.CompanyValidationError
import br.usp.inovacao.hubusp.server.persistence.models.CompanyValidationErrorModel
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import org.litote.kmongo.getCollection

class CompanyErrorRepositoryImpl(
    db : MongoDatabase
) : CompanyErrorRepository {
    private val companyErrorCollection : MongoCollection<CompanyValidationErrorModel>

    init {
        companyErrorCollection = db.getCollection<CompanyValidationErrorModel>("company_errors")
    }

    override fun save(companyError: CompanyValidationError) {
        // TODO: test
        val companyErrorModel = CompanyValidationErrorModel(
            errors = companyError.errors,
            spreadsheetLineNumber = companyError.spreadsheetLineNumber,
            delivered = companyError.delivered
        )

        companyErrorCollection.insertOne(companyErrorModel)
    }
}
