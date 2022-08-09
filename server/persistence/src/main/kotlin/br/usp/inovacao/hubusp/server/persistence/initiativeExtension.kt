package br.usp.inovacao.hubusp.server.persistence

import br.usp.inovacao.hubusp.server.catalog.InitiativeSearchParams

fun InitiativeSearchParams.toCollectionFilter(): String {
    val inner = mutableListOf<String>()

    if (classifications.isNotEmpty()) {
        val orCriteria = classifications.joinToString(",") { "{\"classification\":\"$it\"}" }
        inner.add("\"\$or\":[$orCriteria]")
    }

    if (campus != null) inner.add("\"localization\":\"$campus\"")

    if (term != null) inner.add("\"\$text\":{\"\$search\":\"$term\"}")

    return "{" + inner.joinToString(",") + "}"
}