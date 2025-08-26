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

fun CompanyClassificationModel.toCatalogClassification(): CompanyClassification =
        CompanyClassification(major = this.major, minor = this.minor)

fun CompanyAddressModel.toCatalogAddress(): CompanyAddress =
        CompanyAddress(
                cep = this.cep,
                city = this.city,
                neighborhood = this.neighborhood,
                state = this.state,
                venue = this.venue
        )

fun CompanyModel.toCatalogCompany(): Company =
        Company(
                active = this.active ?: true,
                address = this.address.toCatalogAddress(),
                allowed = this.allowed ?: true,
                classification = this.classification.toCatalogClassification(),
                cnae = this.cnae,
                cnpj = this.cnpj,
                companySize = this.companySize,
                corporateName = this.corporateName ?: "",
                description = this.description,
                ecosystems = this.ecosystems,
                emails = this.emails,
                incubated = this.incubated,
                logo = this.logo,
                name = this.name,
                phones = this.phones,
                services = this.services,
                technologies = this.technologies,
                partners = this.partners.map {
                    Partner(
                        name = it.name,
                        nusp = it.nusp,
                        bond = it.bond,
                        unity = it.unity,
                        email = it.email,
                        phone = it.phone,
                        position = it.position
                    )
                },
                url = this.url,
                year = this.year,
                odss = this.odss,
                linkedin = this.linkedin,
                instagram = this.instagram,
                youtube = this.youtube,
                facebook = this.facebook,
                isUnicorn = this.isUnicorn,
                totalCollaborators = this.totalCollaborators,
                dnaUspWanted = this.dnaUspWanted,
                dnaUspContactEmail = this.dnaUspContactEmail,
                dnaUspContactName = this.dnaUspContactName,
                dnaUspContract = this.dnaUspContract,
                truthfulInformation = this.truthfulInformation,
                agreementOptions = this.agreementOptions ?: emptySet(),
                partnerNames = this.partnerNames ?: emptyList(),
                partnerNusps = this.partnerNusps ?: emptyList(),
                partnerBonds = this.partnerBonds ?: emptyList(),
                partnerUnities = this.partnerUnities ?: emptyList(),
                partnerPositions = this.partnerPositions ?: emptyList(),
                partnerEmails = this.partnerEmails ?: emptyList(),
                partnerPhones = this.partnerPhones ?: emptyList(),
                totalPartners = this.totalPartners,
                hasUspPartners = this.hasUspPartners,
                wantsToAddMorePartners = this.wantsToAddMorePartners,
                cltEmployees = this.cltEmployees ?: 0,
                pjCollaborators = this.pjCollaborators ?: 0,
                internsScholars = this.internsScholars ?: 0,
                hasInvestment = this.hasInvestment ?: false,
                investmentTypes = this.investmentTypes ?: emptySet(),
                ownInvestmentAmount = this.ownInvestmentAmount,
                angelInvestmentAmount = this.angelInvestmentAmount,
                ventureCapitalAmount = this.ventureCapitalAmount,
                privateEquityAmount = this.privateEquityAmount,
                pipeAmount = this.pipeAmount,
                otherInvestmentsAmount = this.otherInvestmentsAmount,
                revenue2022 = this.revenue2022,
                rfbSize = this.rfbSize,
                totalSum = this.totalSum,
                companyType = this.companyType,
                operationalStatus = this.operationalStatus,
                index = this.index,
                incubatorBond = this.incubatorBond,
                uspBondConfirmation = this.uspBondConfirmation,
                uspDnaCategory = this.uspDnaCategory,
                companyBondConfirmation = this.companyBondConfirmation
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
