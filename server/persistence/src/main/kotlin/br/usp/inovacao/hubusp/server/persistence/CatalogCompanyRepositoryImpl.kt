package br.usp.inovacao.hubusp.server.persistence

import br.usp.inovacao.hubusp.server.catalog.Company
import br.usp.inovacao.hubusp.server.catalog.CompanyRepository
import br.usp.inovacao.hubusp.server.catalog.CompanySearchParams
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import kotlinx.serialization.Serializable
import org.litote.kmongo.*

@Serializable
data class Ecosystem(
    val name: String,
)

class CatalogCompanyRepositoryImpl(
    db: MongoDatabase
) : CompanyRepository {
    private val companyCollection: MongoCollection<Company>

    init {
        companyCollection = db.getCollection<Company>("companies")
    }

    override fun filter(params: CompanySearchParams): Set<Company> {
        val filter = params.toCollectionFilter()

        return companyCollection.find(filter).toSet()
    }

    override fun getEcosystems(): Set<String> = companyCollection
        .aggregate<Ecosystem>(
            "[{ \$project: { _id: 0, ecosystems: 1 } }," +
            "{ \$unwind: { path: '\$ecosystems' } }," +
            "{ \$project: { ecosystems: { \$ltrim: { input: '\$ecosystems', chars: ' ' } } } }," +
            "{ \$group: { _id: 'allEcos', name: { \$addToSet: '\$ecosystems' } } }," +
            "{ \$project: { _id: 0, name: 1 } }," +
            "{ \$unwind: { path: '\$name' } }," +
            "{ \$sort: { name: 1 } }]"
        )
        .map { it.name }
        .toSet()
}
