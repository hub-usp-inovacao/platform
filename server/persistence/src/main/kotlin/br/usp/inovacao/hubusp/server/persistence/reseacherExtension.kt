package br.usp.inovacao.hubusp.server.persistence

import br.usp.inovacao.hubusp.server.catalog.ResearcherSearchParams

fun ResearcherSearchParams.toCollectionFilter(): String {
    val inner = mutableListOf<String>()

    if (majorArea.isNotEmpty()) {
        val arrayValue = majorArea
            .joinToString(",") { "{\"area.major\":\"$it\"}" }
        inner.add("\"\$or\":[$arrayValue]")
    }

    if (minorArea.isNotEmpty()) {
        val arrayValue = minorArea
            .joinToString(",") { "{\"area.minors\":\"$it\"}" }
        inner.add("\"\$or\":[$arrayValue]")
    }

    if (campus != null) {
        inner.add("\"campus\":\"$campus\"")
    }

    if (unity != null) {
        inner.add("\"unities\":\"$unity\"")
    }

    if (bond != null) {
        inner.add("\"bond\":\"$bond\"")
    }

    return "{" + inner.joinToString(",") + "}"
}
