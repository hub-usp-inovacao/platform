package br.usp.inovacao.hubusp.catalog.domain

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Suppress("unused")
@kotlinx.serialization.Serializable
class DisciplineQueryFilter private constructor(
    val categories: Map<String, List<String>>? = null,
    val unity: String? = null,
    val campus: String? = null,
    val level: String? = null,
    val nature: String? = null
) {
    companion object {
        fun from(params: Map<String, List<String>>): DisciplineQueryFilter {
            return DisciplineQueryFilter(
                categories = if (params["categories"] != null) mapOf("\$in" to params["categories"]!![0].split(',')) else null,
                unity = params["unity"]?.get(0),
                campus = params["campus"]?.get(0),
                level = params["level"]?.get(0),
                nature = params["nature"]?.get(0)
            )
        }
    }

    fun toJson(): String = Json
        .encodeToString(this)
}
