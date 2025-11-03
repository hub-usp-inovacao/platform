package br.usp.inovacao.hubusp.curatorship.companyform.step

import br.usp.inovacao.hubusp.curatorship.companyform.CompanyFormValidate
import br.usp.inovacao.hubusp.curatorship.companyform.isIn
import br.usp.inovacao.hubusp.curatorship.companyform.isWebsite
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.valiktor.ConstraintViolationException
import org.valiktor.functions.isIn
import org.valiktor.functions.isNotBlank
import org.valiktor.functions.isNotNull
import org.valiktor.functions.isWebsite
import org.valiktor.functions.validate
import org.valiktor.validate

@Serializable
data class AboutCompanyStep(
    val description: String?,
    val logo: String?,
    val services: Set<String> = emptySet(),
    val technologies: Set<String> = emptySet(),
    val site: String?,
    val odss: Set<String> = emptySet(),
    @SerialName("social_medias") val socialMedias: Set<String> = emptySet()
) : CompanyFormValidate {
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
                "17 - Parcerias e Meios de Implementação",
            )
    }

    override fun validate() {
        try {
            validate(this) {
                validate(AboutCompanyStep::description).isNotNull().isNotBlank()
                if (logo != "") {
                    validate(AboutCompanyStep::logo).isWebsite()
                }
                if (site != "") {
                    validate(AboutCompanyStep::site).isWebsite()
                }
                validate(AboutCompanyStep::odss).isIn(AboutCompanyStep.VALID_ODS)
                validate(AboutCompanyStep::socialMedias).isWebsite()
            }
        } catch (cve: ConstraintViolationException) {
            throw StepValidationException.from(cve)
        }
    }
}
