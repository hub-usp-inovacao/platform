package br.usp.inovacao.hubusp.server.persistence.curatorship

import br.usp.inovacao.hubusp.curatorship.sheets.ResearcherErrorRepository
import br.usp.inovacao.hubusp.curatorship.sheets.ResearcherValidationError
import br.usp.inovacao.hubusp.curatorship.sheets.ResearcherUniquenessError
import br.usp.inovacao.hubusp.server.persistence.models.ResearcherValidationErrorModel
import br.usp.inovacao.hubusp.server.persistence.models.ResearcherUniquenessErrorModel
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import org.litote.kmongo.getCollection
import java.time.LocalDate

class ResearcherErrorRepositoryImpl(
    db : MongoDatabase
) : ResearcherErrorRepository {
    private val researcherValidationErrorCollection : MongoCollection<ResearcherValidationErrorModel>
    private val researcherUniquenessErrorCollection : MongoCollection<ResearcherUniquenessErrorModel>

    init {
        researcherValidationErrorCollection = db.getCollection<ResearcherValidationErrorModel>("skill_errors")
        researcherUniquenessErrorCollection = db.getCollection<ResearcherUniquenessErrorModel>("skill_errors")
    }

    override fun save(researcherError: Any) {
        val researcherErrorModel = when (researcherError) {
            is ResearcherValidationError -> ResearcherValidationErrorModel(
                errors = researcherError.errors,
                spreadsheetLineNumber = researcherError.spreadsheetLineNumber,
                delivered = researcherError.delivered
            )
            is ResearcherUniquenessError -> ResearcherUniquenessErrorModel(
                error = researcherError.error,
                delivered = researcherError.delivered
            )
            else -> throw IllegalArgumentException("Invalid researcher error type")
        }

        when (researcherError) {
            is ResearcherValidationError -> researcherValidationErrorCollection.insertOne(researcherErrorModel as ResearcherValidationErrorModel)
            is ResearcherUniquenessError -> researcherUniquenessErrorCollection.insertOne(researcherErrorModel as ResearcherUniquenessErrorModel)
            else -> throw IllegalArgumentException("Invalid researcher error type")
        }
    }
}
