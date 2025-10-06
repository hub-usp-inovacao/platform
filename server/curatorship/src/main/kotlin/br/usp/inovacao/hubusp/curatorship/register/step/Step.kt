package br.usp.inovacao.hubusp.curatorship.register.step

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class Step {
    @SerialName("company_data") CompanyData,
    @SerialName("investment") Investment,
    @SerialName("revenue") Revenue,
    @SerialName("incubation") Incubation,
    @SerialName("dna_usp_stamp") DNAUspStamp,
    @SerialName("staff") Staff,
    @SerialName("partners") Partner,
    @SerialName("about_company") AboutCompany,
}
