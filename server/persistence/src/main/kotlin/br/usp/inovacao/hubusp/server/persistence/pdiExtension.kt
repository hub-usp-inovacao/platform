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

    term?.let {
        val searchQuery = if (it.startsWith("\"") && it.endsWith("\"")) {
            "\\\"${it.removeSurrounding("\"")}\\\""
        } else it

        inner.add("\"\$text\":{\"\$search\":\"$searchQuery\"}")
    }

    return "{" + inner.joinToString(",") + "}"
}