package br.usp.inovacao.hubusp.curatorship.sheets

import br.usp.inovacao.hubusp.curatorship.sheets.Initiative.Companion.propertyToIndex
import it.skrape.core.*
import it.skrape.fetcher.*
import it.skrape.selects.html5.*
import org.valiktor.ConstraintViolationException
import org.valiktor.functions.isNotBlank
import org.valiktor.functions.isNotNull
import org.valiktor.i18n.mapToMessage
import org.valiktor.validate
import br.usp.inovacao.hubusp.curatorship.sheets.utils.indexToColumnLetter

@kotlinx.serialization.Serializable
data class DisciplineCategory(
    val business: Boolean?,
    val entrepreneurship: Boolean?,
    val innovation: Boolean?,
    val intellectualProperty: Boolean?
){
    companion object{
        fun fromRow(subRow: List<String?>) = DisciplineCategory(
            business = subRow[0]?.equals("x", ignoreCase = true) ?: false,
            entrepreneurship = subRow[1]?.equals("x", ignoreCase = true) ?: false,
            innovation = subRow[2]?.equals("x", ignoreCase = true) ?: false,
            intellectualProperty = subRow[3]?.equals("x", ignoreCase = true) ?: false
        )
    }
}

@kotlinx.serialization.Serializable
data class Discipline(
    val name: String?,
    val campus: String?,
    val unity: String?,
    val startDate: String?,
    val nature: String?,
    val level: String?,
    val url: String?,
    val description: String?,
    val category: DisciplineCategory,
    val keywords: Set<String>?,
    val offeringPeriod: String?,
    val beingOffered: Boolean,
    val offerings: Set<DisciplineOffering> = emptySet(),
) {
    companion object {

        val natures = listOf("Graduação", "Pós-graduação")
        val levels = listOf("Preciso testar minha ideia!", "Quero aprender!", "Tenho uma ideia, e agora?", "Tópicos avançados em Empreendedorismo")

        val propertyToIndex: Map<String, Int> = mapOf(
            "nature" to 0,
            "name" to 1,
            "campus" to 2,
            "unity" to 3,
            "url" to 4,
            "level" to 5,
            "description" to 6,
            "startDate" to 8,
            "category" to 9,
            "offeringPeriod" to 13,
        )

        val propertyToColumn = propertyToIndex.mapValues { indexToColumnLetter(it.value) }

        fun createKeywords(subRow: List<String?>) : Set<String>? {

            var keywords = arrayListOf<String>()

            if(subRow[0] == "x") keywords.add("Negócios")
            if(subRow[1] == "x") keywords.add("Empreendedorismo")
            if(subRow[2] == "x") keywords.add("Inovação")
            if(subRow[3] == "x") keywords.add("Propriedade Intelectual")

            return keywords.toSet()
        }


        /**
         * Removes the query parameter 'verdis' from Jupiter URLs, otherwise it goes to an outdated
         * page.
         */
        fun fixJupiterUrl(url: String?) = url?.replace(Regex("&?verdis=[^&]+"), "")

        fun fromRow(subRow: List<String?>): Discipline {
            val offerings = fetchOffering(subRow[propertyToIndex["name"]!!], subRow[propertyToIndex["nature"]!!])
            val categoryRow = subRow.subList(propertyToIndex["category"]!!, propertyToIndex["category"]!! + 4)

            return Discipline(
                name = subRow[propertyToIndex["name"]!!],
                campus = subRow[propertyToIndex["campus"]!!],
                unity = subRow[propertyToIndex["unity"]!!],
                startDate = subRow[propertyToIndex["startDate"]!!],
                nature = subRow[propertyToIndex["nature"]!!],
                level = subRow[propertyToIndex["level"]!!],
                url = fixJupiterUrl(subRow[propertyToIndex["url"]!!]),
                description = subRow[propertyToIndex["description"]!!],
                category = DisciplineCategory.fromRow(categoryRow),
                keywords = createKeywords(categoryRow),
                offeringPeriod = subRow[propertyToIndex["offeringPeriod"]!!],
                beingOffered = offerings.isNotEmpty(),
                offerings,
            )
        }

        fun fetchOffering(
            name: String?,
            nature: String?,
            delayBetweenRequests: Long = 2000L
        ): Set<DisciplineOffering> {
            val code = name?.split(" - ")?.getOrNull(0)
            if (code == null) return emptySet()

            Thread.sleep(delayBetweenRequests)

            return if (nature == "Graduação") {
                DisciplineOffering.trySetFromJupiter(code)
            } else {
                DisciplineOffering.trySetFromJanus(code)
            }
        }
    }

    init{
        try{
            validate(this){
                validate(Discipline::name)
                    .isNotNull()
                    .isNotBlank()
                    .isValidName()
                validate(Discipline::campus)
                    .isNotNull()
                    .isCampus()
                validate(Discipline::unity)
                    .isNotNull()
                    .isUnity()
                validate(Discipline::nature)
                    .isNotNull()
                    .isValidNature()
                validate(Discipline::level)
                    .isNotNull()
                    .isValidLevel()
                validate(Discipline::url)
                    .isNotNull()
                    .isNotBlank()
                validate(Discipline::description)
                    .isNotNull()
                    .isNotBlank()
                validate(Discipline::category)
                    .isNotNull()
                    .isValidCategory()
            }
        }
        catch (cve: ConstraintViolationException) {
            val violations: List<String> = cve.constraintViolations
                .mapToMessage(baseName = "messages")
                .map { "${it.property}: ${it.message}" }

            throw ValidationException(messages = violations)
        }
    }
}
