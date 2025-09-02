package br.usp.inovacao.hubusp.curatorship.register

import br.usp.inovacao.hubusp.curatorship.register.step.AboutCompanyStep
import br.usp.inovacao.hubusp.curatorship.register.step.Step
import br.usp.inovacao.hubusp.curatorship.register.step.StepValidationException
import kotlinx.serialization.Serializable
import org.valiktor.functions.validate
import org.valiktor.validate

@Serializable
data class CompanyForm(
    // TODO
    // val partners: PartnerStep
    //
    // val data: CompanyDataStep,
    val about: AboutCompanyStep,
    // val incubation: IncubationStep,
    // val staff: StaffStep,
    // val revenue: RevenueStep,
    // val investment: InvestmentStep,
    //
    // val dnaUsp: dnaUspStampStep,
) {
    init {
        val errorMap: MutableMap<Step, List<String>> = mutableMapOf()

        try {
            this.about.validate()
        } catch (e: StepValidationException) {
            errorMap.put(Step.AboutCompany, e.messages)
        }

        if (errorMap.isNotEmpty()) {
            throw CompanyFormValidationException(errorMap)
        }
    }
}
