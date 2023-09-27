package br.usp.inovacao.hubusp.server.persistence.curatorship

import br.usp.inovacao.hubusp.curatorship.sheets.Company
import br.usp.inovacao.hubusp.curatorship.sheets.CompanyRepository
import br.usp.inovacao.hubusp.server.persistence.models.CompanyModel
import br.usp.inovacao.hubusp.server.persistence.models.PartnerModel
import br.usp.inovacao.hubusp.server.persistence.models.CompanyClassificationModel
import br.usp.inovacao.hubusp.server.persistence.models.CompanyAddressModel
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters
import org.litote.kmongo.getCollection
import java.time.LocalDateTime

class CompanyRepositoryImpl(
    db: MongoDatabase
) : CompanyRepository {
    private val companyCollection : MongoCollection<CompanyModel>

    init {
        companyCollection = db.getCollection<CompanyModel>("companies")
    }

    override fun save(company: Company) {
        // TODO: test
        // TODO: improve CompanyModel to follow validation structure (avoid parsing null)
        // TODO-ze: fix this with required fields

        val companyClassificationModel = CompanyClassificationModel(
            major = company.classification.major ?: "",
            minor = company.classification.minor ?: ""
        )

        val companyAddressModel = CompanyAddressModel(
            cep = company.address.cep ?: "",
            city = company.address.city ?: "",
            neighborhood = company.address.neighborhood ?: "",
            state = company.address.state ?: "",
            venue = company.address.venue ?: ""
        )

        val partnerModels = company.partners?.map { partner ->
            PartnerModel(
                name = partner.name ?: "",
                nusp = partner.nusp ?: "",
                bond = partner.bond ?: "",
                unity = partner.unity ?: "",
                email = partner.email ?: "",
                phone = partner.phone ?: ""
            )
        }?.toSet()

        val companyModel = CompanyModel(
           active = company.active ?: true,
           address = companyAddressModel,
           allowed = company.allowed ?: true,
           classification = companyClassificationModel,
           cnae = company.cnae ?: "",
           cnpj = company.cnpj ?: "",
           companySize = company.companySize ?: emptySet(),
           corporateName = company.corporateName ?: "",
           description = company.description ?: "",
           ecosystems = company.ecosystems ?: emptySet(),
           emails = company.emails ?: emptySet(),
           incubated = company.incubated ?: "",
           logo = company.logo ?: "",
           name = company.name ?: "",
           phones = company.phones ?: emptySet(),
           services = company.services ?: emptySet(),
           technologies = company.technologies  ?: emptySet(),
           partners = partnerModels ?: emptySet(),
           url = company.url ?: "",
           year = company.year ?: ""
        )

        companyCollection.insertOne(companyModel)
    }
    override fun clean() {
        val currentTime = LocalDateTime.now()

        val fiveSecondsAgo = currentTime.minusSeconds(30)

        val filter = Filters.lt("timestamp", fiveSecondsAgo)

        companyCollection.deleteMany(filter)
    }
}
