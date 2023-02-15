package br.usp.inovacao.hubusp.server.persistence

import br.usp.inovacao.hubusp.server.catalog.Category
import br.usp.inovacao.hubusp.server.catalog.Description
import br.usp.inovacao.hubusp.server.catalog.Discipline
import br.usp.inovacao.hubusp.server.catalog.DisciplineSearchParams
import br.usp.inovacao.hubusp.server.persistence.models.DisciplineCategory
import br.usp.inovacao.hubusp.server.persistence.models.DisciplineDescription
import br.usp.inovacao.hubusp.server.persistence.models.DisciplineModel
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import org.litote.kmongo.find
import org.litote.kmongo.getCollection

fun DisciplineCategory.toCatalogCategory(): Category = Category(
    innovation = this.innovation,
    business = this.business,
    entrepreneurship = this.entrepreneurship,
    intellectual_property = this.intellectual_property
)

fun DisciplineDescription.toCatalogDescription(): Description = Description(
    long = this.long,
    short = this.short
)

fun DisciplineModel.toCatalogDiscipline(): Discipline = Discipline(
    name = this.name,
    category = this.category.toCatalogCategory(),
    description = this.description.toCatalogDescription(),
    unity = this.unity,
    campus = this.campus,
    level = this.level,
    nature = this.nature,
    url = this.url
)

class CatalogDisciplineRepositoryImpl(
    db: MongoDatabase
) : br.usp.inovacao.hubusp.server.catalog.DisciplineRepository {

    private val collection: MongoCollection<DisciplineModel>

    init {
        collection = db.getCollection<DisciplineModel>("disciplines")
    }

    override fun filter(params: DisciplineSearchParams): Set<Discipline> {
        return collection
            .find(params.toCollectionFilter())
            .map { it.toCatalogDiscipline() }
            .toSet()
    }
}
