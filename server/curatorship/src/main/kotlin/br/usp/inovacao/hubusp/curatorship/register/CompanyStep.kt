package br.usp.inovacao.hubusp.curatorship.register

import kotlinx.serialization.Serializable
import org.valiktor.ConstraintViolationException
import org.valiktor.functions.isNotBlank
import org.valiktor.functions.isNotNull
import org.valiktor.i18n.mapToMessage
import org.valiktor.validate

@Serializable
data class CompanyStep(
    // TO DO
    //val data: CompanyDataStep,
    //val investment: InvestmentStep,
    //val revenue: RevenueStep,
    //val incubation: IncubationStep,
    //val dnaUsp: dnaUspStampStep,
    //val staff: StaffStep,
    //val partners: PartnerStep
    val about: AboutCompanyStep,
)

@Serializable
data class AboutCompanyStep(
    val description: String,
    val logo: String,
    val services: Set<String>,
    val technologies: Set<String>,
    val site: String,
    val odss: Set<String>,
    val socialMedia: Set<String>
) {
    init {
        try {
            validate(this) {
                validate(AboutCompanyStep::description)
                    .isNotNull()
                    .isNotBlank()
            }
        }
        catch (cve: ConstraintViolationException) {
            val violations: List<String> = cve.constraintViolations
                .mapToMessage(baseName = "ErrorMessages")
                .map { "${it.property} : ${it.message}" }
        }
    }
}