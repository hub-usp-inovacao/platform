package br.usp.inovacao.hubusp.curatorship.sheets

import it.skrape.core.*
import it.skrape.fetcher.*
import it.skrape.selects.html5.*
import org.valiktor.ConstraintViolationException
import org.valiktor.functions.isNotBlank
import org.valiktor.functions.isNotNull
import org.valiktor.i18n.mapToMessage
import org.valiktor.validate

@kotlinx.serialization.Serializable
data class DisciplineCategory(
    val business: Boolean?,
    val entrepreneurship: Boolean?,
    val innovation: Boolean?,
    val intellectual_property: Boolean?
){
    companion object{
        fun fromRow(subRow: List<String?>) = DisciplineCategory(
            business = subRow[9]?.equals("x", ignoreCase = true) ?: false,
            entrepreneurship = subRow[10]?.equals("x", ignoreCase = true) ?: false,
            innovation = subRow[11]?.equals("x", ignoreCase = true) ?: false,
            intellectual_property = subRow[12]?.equals("x", ignoreCase = true) ?: false
        )
    }
}

@kotlinx.serialization.Serializable
data class Discipline(
    val name: String?,
    val campus: String?,
    val unity: String?,
    val start_date: String?,
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

        fun createKeywords(subRow: List<String?>) : Set<String>? {

            var keywords = arrayListOf<String>()

            if(subRow[9] == "x") keywords.add("Negócios")
            if(subRow[10] == "x") keywords.add("Empreendedorismo")
            if(subRow[11] == "x") keywords.add("Inovação")
            if(subRow[12] == "x") keywords.add("Propriedade Intelectual")

            return keywords.toSet()
        }

        /**
         * Removes the query parameter 'verdis' from Jupiter URLs, otherwise it goes to an outdated
         * page.
         */
        fun fixJupiterUrl(url: String?) = url?.replace(Regex("&?verdis=[^&]+"), "")

        fun fromRow(subRow: List<String?>): Discipline {
            val offerings = fetchOffering(subRow[1], subRow[0])

            return Discipline(
                name = subRow[1],
                campus = subRow[2],
                unity = subRow[3],
                start_date = subRow[8],
                nature = subRow[0],
                level = subRow[5],
                url = fixJupiterUrl(subRow[4]),
                description = subRow[6],
                category = DisciplineCategory.fromRow(subRow),
                keywords = createKeywords(subRow),
                offeringPeriod = subRow[13],
                beingOffered = offerings.isNotEmpty(),
                offerings,
            )
        }

        fun fetchOffering(name: String?, nature: String?): Set<DisciplineOffering> {
            val code = name?.split(" - ")?.getOrNull(0)

            if (code == null) return emptySet()

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
