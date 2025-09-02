package br.usp.inovacao.hubusp.server.persistence

import br.usp.inovacao.hubusp.server.catalog.CompanySearchParams

fun CompanySearchParams.toCollectionFilter(): String {
    val inner = mutableListOf<String>()

    if (areaMajors.isNotEmpty()) {
        val orCriteria = areaMajors.joinToString(",") {
            "{\"classification.major\":\"$it\"}"
        }
        inner.add("\"\$or\":[$orCriteria]")
    }

    if (areaMinors.isNotEmpty()) {
        val orCriteria = areaMinors.joinToString(",") {
            "{\"classification.minor\":\"$it\"}"
        }
        inner.add("\"\$or\":[$orCriteria]")
    }

    if (unity != null) inner.add("\"partners.unity\":\"$unity\"")

    if (city != null) inner.add("\"address.city\":\"$city\"")

    if (ecosystem != null) inner.add("\"ecosystems\":\"$ecosystem\"")

    if (size != null) inner.add("\"companySize\":\"$size\"")

    term?.let {
        val searchQuery = if (it.startsWith("\"") && it.endsWith("\"")) {
            "\\\"${it.removeSurrounding("\"")}\\\""
        } else it

        inner.add("\"\$text\":{\"\$search\":\"$searchQuery\"}")
    }

    return "{" + inner.joinToString(",") + "}"
}
