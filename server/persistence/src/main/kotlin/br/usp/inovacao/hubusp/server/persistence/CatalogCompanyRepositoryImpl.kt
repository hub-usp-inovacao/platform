package br.usp.inovacao.hubusp.server.persistence

import br.usp.inovacao.hubusp.server.catalog.Company
import br.usp.inovacao.hubusp.server.catalog.CompanyRepository
import br.usp.inovacao.hubusp.server.catalog.CompanySearchParams
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import org.litote.kmongo.find
import org.litote.kmongo.getCollection

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
}
