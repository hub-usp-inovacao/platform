package br.usp.inovacao.hubusp.server.persistence.curatorship

import br.usp.inovacao.hubusp.curatorship.sheets.DisciplineErrorRepository
import br.usp.inovacao.hubusp.curatorship.sheets.DisciplineValidationError
import br.usp.inovacao.hubusp.server.persistence.models.DisciplineValidationErrorModel
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import org.litote.kmongo.getCollection

class DisciplineErrorRepositoryImpl(
    db: MongoDatabase
) : DisciplineErrorRepository {

    private val disciplineErrorCollection : MongoCollection<DisciplineValidationErrorModel>

    init {
        disciplineErrorCollection = db.getCollection<DisciplineValidationErrorModel>("discipline_errors")
    }

    override fun save(disciplineError: DisciplineValidationError) {
        val disciplineErrors = DisciplineValidationErrorModel(
            errors = disciplineError.errors,
            spreadsheetLineNumber = disciplineError.spreadsheetLineNumber,
            delivered = disciplineError.delivered
        )

        disciplineErrorCollection.insertOne(disciplineErrors)
    }

}