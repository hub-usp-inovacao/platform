package br.usp.inovacao.hubusp.server.persistence

import br.usp.inovacao.hubusp.server.catalog.DisciplineSearchParams

fun DisciplineSearchParams.toCollectionFilter(): String {
    val inner = mutableListOf<String>()

    if (categories.isNotEmpty()) inner.add(handleCategories(categories))

    if (unity != null) inner.add("\"unity\":\"$unity\"")
    if (campus != null) inner.add("\"campus\":\"$campus\"")
    if (level != null) inner.add("\"level\":\"$level\"")
    if (nature != null) inner.add("\"nature\":\"$nature\"")
    if (offeringPeriod != null) inner.add("\"offeringPeriod\":\"$offeringPeriod\"")

    if (term != null) inner.add("\"\$text\":{\"\$search\":\"$term\"}")

    return "{" + inner.joinToString(",") + "}"
}

private fun handleCategories(categories: Set<String>): String {
    val conditions = mutableListOf<String>()

    if (categories.contains("Negócios")) conditions.add("{\"category.business\":true}")

    if (categories.contains("Empreendedorismo")) conditions.add("{\"category.entrepreneurship\":true}")

    if (categories.contains("Propriedade Intelectual")) conditions.add("{\"category.intellectual_property\":true}")

    if (categories.contains("Inovação")) conditions.add("{\"category.innovation\":true}")

    return "\"\$or\":[${conditions.joinToString(",")}]"
}
