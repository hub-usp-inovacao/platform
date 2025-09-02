package br.usp.inovacao.hubusp.curatorship.register.step

import br.usp.inovacao.hubusp.curatorship.register.isIn
import br.usp.inovacao.hubusp.curatorship.register.isWebsite
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.valiktor.ConstraintViolationException
import org.valiktor.functions.isIn
import org.valiktor.functions.isNotBlank
import org.valiktor.functions.isNotNull
import org.valiktor.functions.isWebsite
import org.valiktor.functions.validate
import org.valiktor.i18n.mapToMessage
import org.valiktor.validate

@Serializable
data class AboutCompanyStep(
    val description: String?,
    val logo: String?,
    val services: Set<String>,
    val technologies: Set<String>,
    val site: String?,
    val odss: Set<String>,
    @SerialName("social_medias") val socialMedias: Set<String>
) {
    companion object {
        val VALID_ODS =
            listOf(
                "1 - Erradicação da Pobreza",
                "2 - Fome Zero",
                "3 - Saúde e Bem Estar",
                "4 - Educação de Qualidade",
                "5 - Igualdade de Gênero",
                "6 - Água Potável e Saneamento",
                "7 - Energia Limpa e Acessível",
                "8 - Trabalho Decente e Crescimento Econômico",
                "9 - Indústria, Inovação e Infraestrutura",
                "10 - Redução das Desigualdades",
                "11 - Cidades e Comunidades Sustentáveis",
                "12 - Consumo e Produção Responsáveis",
                "13 - Ação Contra a Mudança Global do Clima",
                "14 - Vida na Água",
                "15 - Vida Terrestre",
                "16 - Paz, Justiça e Instituições Eficazes",
                "17 - Parcerias e Meios de Implementação")
    }

    @Throws(StepValidationException::class)
    fun validate() =
        try {
            validate(this) {
                validate(AboutCompanyStep::description).isNotNull().isNotBlank()
                validate(AboutCompanyStep::logo).isNotNull().isNotBlank().isWebsite()
                validate(AboutCompanyStep::site).isNotNull().isNotBlank().isWebsite()
                validate(AboutCompanyStep::odss).isIn(AboutCompanyStep.VALID_ODS)
                validate(AboutCompanyStep::socialMedias).isWebsite()
            }
        } catch (cve: ConstraintViolationException) {
            // TODO: Set locale to pt_BR
            val violations: List<String> =
                cve.constraintViolations.mapToMessage(baseName = "messages").map {
                    "${it.property}: ${it.message}"
                }
            throw StepValidationException(messages = violations)
        }
}
