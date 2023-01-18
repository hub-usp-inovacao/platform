package br.usp.inovacao.hubusp.server.persistence

import br.usp.inovacao.hubusp.server.catalog.Contact
import br.usp.inovacao.hubusp.server.catalog.Initiative
import br.usp.inovacao.hubusp.server.catalog.InitiativeRepository
import br.usp.inovacao.hubusp.server.catalog.InitiativeSearchParams
import br.usp.inovacao.hubusp.server.persistence.models.InitiativeContact
import br.usp.inovacao.hubusp.server.persistence.models.InitiativeModel
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import org.litote.kmongo.find
import org.litote.kmongo.getCollection

fun InitiativeContact.toCatalogContact(): Contact = Contact(
    info = this.info,
    person = this.person
)

fun InitiativeModel.toCatalogInitiative(): Initiative = Initiative(
    classification = this.classification,
    contact = this.contact.toCatalogContact(),
    description = this.description,
    email = this.email,
    localization = this.localization,
    name = this.name,
    tags = this.tags,
    unity = this.unity,
    url = this.url
)

class CatalogInitiativeRepositoryImpl(
    db: MongoDatabase
): InitiativeRepository {
    private val initiativeCollection : MongoCollection<InitiativeModel>

    init {
        initiativeCollection = db.getCollection<InitiativeModel>("iniciatives")
    }

    override fun filter(params: InitiativeSearchParams): Set<Initiative> {
        val filter = params.toCollectionFilter()

        return initiativeCollection
            .find(filter)
            .map { it.toCatalogInitiative() }
            .toSet()
    }
}