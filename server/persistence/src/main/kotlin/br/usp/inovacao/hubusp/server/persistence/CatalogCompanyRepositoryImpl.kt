package br.usp.inovacao.hubusp.server.persistence

import br.usp.inovacao.hubusp.server.catalog.*
import br.usp.inovacao.hubusp.server.persistence.models.CompanyAddressModel
import br.usp.inovacao.hubusp.server.persistence.models.CompanyClassificationModel
import br.usp.inovacao.hubusp.server.persistence.models.CompanyModel
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import kotlinx.serialization.Serializable
import org.litote.kmongo.*

@Serializable
data class Ecosystem(
        val name: String,
)

@Serializable data class City(val name: String)

fun CompanyClassificationModel.toCatalogClassification(): Classification =
        Classification(major = this.major, minor = this.minor)

fun CompanyAddressModel.toCatalogAddress(): Address =
        Address(
                cep = this.cep,
                city = this.city,
                neighborhood = this.neighborhood,
                state = this.state,
                venue = this.venue
        )

fun CompanyModel.toCatalogCompany(): Company =
        Company(
                address = this.address.toCatalogAddress(),
                classification = this.classification.toCatalogClassification(),
                cnae = this.cnae,
                companySize = this.companySize,
                description = this.description,
                ecosystems = this.ecosystems,
                emails = this.emails,
                incubated = this.incubated,
                logo = this.logo,
                name = this.name,
                phones = this.phones,
                services = this.services,
                technologies = this.technologies,
                unities = this.partners.map { it.unity }.toSet(),
                url = this.url
        )

class CatalogCompanyRepositoryImpl(db: MongoDatabase) : CompanyRepository {
    private val companyCollection: MongoCollection<CompanyModel>

    init {
        companyCollection = db.getCollection<CompanyModel>("companies")
    }

    override fun filter(params: CompanySearchParams): Set<Company> {
        val filter = params.toCollectionFilter()

        return companyCollection.find(filter).map { it.toCatalogCompany() }.toSet()
    }

    override fun getEcosystems(): Set<String> =
            companyCollection
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

    override fun getCities(): Set<String> =
            companyCollection
                    .aggregate<City>(
                            "[{ \$project: { _id: 0, city: '\$address.city' } }," +
                                    "{ \$project: { city: { \$ltrim: { input: '\$city', chars: ' ' } } } }," +
                                    "{ \$group: { _id: 'allCities', name: { \$addToSet: '\$city' } } }," +
                                    "{ \$project: { _id: 0, name: 1 } }," +
                                    "{ \$unwind: { path: '\$name' } }," +
                                    "{ \$sort: { name: 1 } }]"
                    )
                    .map { it.name }
                    .filter { it.isNotBlank() }
                    .toSet()
}
