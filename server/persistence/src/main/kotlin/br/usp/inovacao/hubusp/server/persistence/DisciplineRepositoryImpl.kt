package br.usp.inovacao.hubusp.server.persistence

import br.usp.inovacao.hubusp.server.catalog.Category
import br.usp.inovacao.hubusp.server.catalog.Discipline
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.litote.kmongo.find
import org.litote.kmongo.getCollection

class DisciplineQueryFilter private constructor(
    val category: Map<String, List<Map<String, Boolean>>>? = null,
    val unity: String? = null,
    val campus: String? = null,
    val level: String? = null,
    val nature: String? = null
) {
    companion object {
        fun from(params: Map<String, List<String>>): DisciplineQueryFilter {
            return DisciplineQueryFilter(
                category = parseCategories(params["categories"]),
                unity = params["unity"]?.get(0),
                campus = params["campus"]?.get(0),
                level = params["level"]?.get(0),
                nature = params["nature"]?.get(0)
            )
        }

        private fun parseCategories(categories: List<String>?): Map<String, List<Map<String, Boolean>>>? {
            if (categories == null) return null

            val ptBrCategoryList = categories[0].split(',')

            val conditions = mutableListOf<Map<String, Boolean>>()

            if (ptBrCategoryList.contains("Negócios")) conditions.add(
                mapOf("category.business" to true)
            )

            if (ptBrCategoryList.contains("Empreendedorismo")) conditions.add(
                mapOf("category.entrepreneurship" to true)
            )

            if (ptBrCategoryList.contains("Propriedade Intelectual")) conditions.add(
                mapOf("category.intellectual_property" to true)
            )

            if (ptBrCategoryList.contains("Inovação")) conditions.add(
                mapOf("category.innovation" to true)
            )

            return mapOf("\$or" to conditions)
        }
    }

    fun toJson(): String {
        val rawJson: MutableList<String> = mutableListOf()

        if (unity != null) rawJson.add("unity:\"$unity\"")
        if (campus != null) rawJson.add("campus:\"$campus\"")
        if (level != null) rawJson.add("level:\"$level\"")
        if (nature != null) rawJson.add("nature:\"$nature\"")

        if (category != null) {
            val conditionsJson = Json.encodeToString(category["\$or"])
            rawJson.add("\$or:$conditionsJson")
        }

        return "{" + rawJson.joinToString(",") + "}"
    }
}

class CatalogDisciplineRepositoryImpl(
    db: MongoDatabase
) : br.usp.inovacao.hubusp.server.catalog.DisciplineRepository {

    private val collection: MongoCollection<Discipline>

    init {
        collection = db.getCollection<Discipline>("disciplines")
        collection.insertMany(
            listOf(
                Discipline(
                    name = "MAC0218",
                    unity = "IME",
                    campus = "Butantã",
                    level = "Quero aprender!",
                    nature = "Graduação",
                    category = Category(
                        innovation = true,
                        business = false,
                        intellectual_property = false,
                        entrepreneurship = false
                    )
                ),
                Discipline(
                    name = "MAC0219",
                    unity = "IME",
                    campus = "Butantã",
                    level = "Quero aprender!",
                    nature = "Graduação",
                    category = Category(
                        innovation = true,
                        business = false,
                        intellectual_property = false,
                        entrepreneurship = false
                    )
                )
            )
        )
    }

    override fun filter(params: Map<String, List<String>>): Set<Discipline> {
        val filter = DisciplineQueryFilter.from(params).toJson()

        return collection.find(filter).toSet()
    }
}