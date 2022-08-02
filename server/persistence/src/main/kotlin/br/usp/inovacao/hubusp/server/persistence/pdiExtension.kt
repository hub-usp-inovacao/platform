package br.usp.inovacao.hubusp.server.persistence

import br.usp.inovacao.hubusp.server.catalog.PDISearchParams

fun PDISearchParams.toCollectionFilter(): String {
    val inner = mutableListOf<String>()

    if (categories.isNotEmpty()) {
        val orCriteria = categories.joinToString(",") {
            "{\"category\":\"$it\"}"
        }

        inner.add("\"\$or\":[$orCriteria]")
    }

    if (campus != null) inner.add("\"campus\":\"$campus\"")

    return "{" + inner.joinToString(",") + "}"
}