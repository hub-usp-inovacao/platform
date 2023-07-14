package br.usp.inovacao.hubusp.server.persistence

import br.usp.inovacao.hubusp.server.catalog.PDI
import br.usp.inovacao.hubusp.server.catalog.PDIRepository
import br.usp.inovacao.hubusp.server.catalog.PDISearchParams
import br.usp.inovacao.hubusp.server.catalog.Time
import br.usp.inovacao.hubusp.server.persistence.models.PDIModel
import br.usp.inovacao.hubusp.server.persistence.models.PDITime
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import org.litote.kmongo.find
import org.litote.kmongo.getCollection

fun PDIModel.toCatalogPDI(): PDI = PDI(
    category = this.category,
    name = this.name,
    campus = this.campus,
    unity = this.unity,
    coordinator = this.coordinator,
    site = this.site,
    email = this.email,
    phone = this.phone,
    description = this.description,
    tags = this.tags,
    timestamp = this.timestamp.toCatalogPDITime(),
)

private fun PDITime.toCatalogPDITime(): Time = Time(
    time = this.time,
)


class CatalogPDIRepositoryImpl(
    db: MongoDatabase
) : PDIRepository {

    private val pdiCollection: MongoCollection<PDIModel>

    init {
        pdiCollection = db.getCollection<PDIModel>("pdis")
    }

    override fun filter(params: PDISearchParams): Set<PDI> {
        val filter: String = params.toCollectionFilter()

        return pdiCollection
            .find(filter)
            .map { it.toCatalogPDI() }
            .toSet()

    }
}