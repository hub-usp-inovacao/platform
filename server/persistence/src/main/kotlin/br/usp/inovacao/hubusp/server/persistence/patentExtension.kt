package br.usp.inovacao.hubusp.server.persistence

import br.usp.inovacao.hubusp.server.catalog.PatentSearchParams

fun PatentSearchParams.toCollectionFilter(): String {
    val inner = mutableListOf<String>()

    if (majorAreas.isNotEmpty()) {
        val orCriteria = majorAreas.joinToString(",") {
            "{\"classification.primary.cip\":\"$it\"},{\"classification.secondary.cip\":\"$it\"}"
        }
        inner.add("\"\$or\":[$orCriteria]")
    }

    if (minorAreas.isNotEmpty()) {
        val orCriteria = minorAreas.joinToString(",") {
            "{\"classification.primary.subarea\":\"$it\"},{\"classification.secondary.subarea\":\"$it\"}"
        }
        inner.add("\"\$or\":[$orCriteria]")
    }

    if (status != null) inner.add("\"status\":\"$status\"")

    return "{" + inner.joinToString(",") + "}"
}