package br.usp.inovacao.hubusp.curatorship.sheets

import kotlinx.serialization.Contextual
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.valiktor.Constraint
import org.valiktor.Validator
import org.valiktor.ConstraintViolationException
import org.valiktor.functions.*
import org.valiktor.i18n.mapToMessage
import org.valiktor.validate
import java.time.LocalDate
import it.skrape.core.*
import it.skrape.fetcher.*
import it.skrape.selects.html5.*

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
    val beingOffered: Boolean
){
    companion object{

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

        fun fromRow(subRow: List<String?>) = Discipline(
            name = subRow[1],
            campus = subRow[2],
            unity = subRow[3],
            start_date = subRow[8],
            nature = subRow[0],
            level = subRow[5],
            url = subRow[4],
            description = subRow[6],
            category = DisciplineCategory.fromRow(subRow),
            keywords = createKeywords(subRow),
            offeringPeriod = subRow[13],
            beingOffered = checkIfOffering(subRow[1], subRow[0])
        )

        fun checkIfOffering(name : String?, nature: String?) : Boolean {
            val code = name?.split(" - ")?.get(0)
            val jupiterUrl = "https://uspdigital.usp.br/jupiterweb/obterTurma?sgldis=${code}"
            val janusUrl = "https://uspdigital.usp.br/janus/DisciplinaAux?tipo=T&sgldis=${code}"
            var scrap = false
            if(nature == "Graduação"){
                skrape(HttpFetcher) {
                    request {
                        url = jupiterUrl
                    }
                    response {
                        htmlDocument {
                            val text = findFirst("div#my_web_cabecalho").text
                            if (text == "Disciplinas oferecidas") {
                                scrap = true
                            }
                        }
                    }
                }
            }
            else if(nature == "Pós-graduação"){
                skrape(HttpFetcher){
                    request {
                        url = janusUrl
                    }
                    response {
                        htmlDocument {
                            try{
                                val found = td {
                                    findFirst {
                                        b {
                                            findFirst {
                                                text
                                            }
                                        }
                                    }
                                }
                                scrap = true
                            }catch (_: Exception){
                            }
                        }
                    }
                }
            }
            return scrap
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