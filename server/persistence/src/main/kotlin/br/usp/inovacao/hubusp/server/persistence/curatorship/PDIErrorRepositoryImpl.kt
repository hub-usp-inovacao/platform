package br.usp.inovacao.hubusp.server.persistence.curatorship

import br.usp.inovacao.hubusp.curatorship.sheets.PDIErrorRepository
import br.usp.inovacao.hubusp.curatorship.sheets.PDIValidationError
import br.usp.inovacao.hubusp.server.persistence.models.PDIValidationErrorModel
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters
import org.litote.kmongo.getCollection

class PDIErrorRepositoryImpl(
    db : MongoDatabase
) : PDIErrorRepository {
    private val pdiErrorCollection : MongoCollection<PDIValidationErrorModel>

    init {
        pdiErrorCollection = db.getCollection<PDIValidationErrorModel>("pdi_errors")
    }

    override fun save(pdiError: PDIValidationError) {
        // TODO: test
        val pdiErrorModel = PDIValidationErrorModel(
            errors = pdiError.errors,
            spreadsheetLineNumber = pdiError.spreadsheetLineNumber,
            delivered = pdiError.delivered
        )

        pdiErrorCollection.insertOne(pdiErrorModel)
    }

    override fun clean(){
        pdiErrorCollection.deleteMany(Filters.empty())
    }
}