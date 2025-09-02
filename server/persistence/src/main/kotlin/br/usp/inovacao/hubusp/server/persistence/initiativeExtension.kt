package br.usp.inovacao.hubusp.server.persistence

import br.usp.inovacao.hubusp.server.catalog.InitiativeSearchParams

fun InitiativeSearchParams.toCollectionFilter(): String {
    val inner = mutableListOf<String>()

    if (classifications.isNotEmpty()) {
        val orCriteria = classifications.joinToString(",") { "{\"classification\":\"$it\"}" }
        inner.add("\"\$or\":[$orCriteria]")
    }

    if (campus != null) inner.add("\"localization\":\"$campus\"")

    term?.let {
        val searchQuery = if (it.startsWith("\"") && it.endsWith("\"")) {
            "\\\"${it.removeSurrounding("\"")}\\\""
        } else it

        inner.add("\"\$text\":{\"\$search\":\"$searchQuery\"}")
    }

    return "{" + inner.joinToString(",") + "}"
}