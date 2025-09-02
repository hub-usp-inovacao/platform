package br.usp.inovacao.hubusp.curatorship.register.step

import kotlinx.serialization.Serializable

// TODO: Serialize to snake_case JSON
//
// CompanyData -> company_data
// Investment -> about_company
// Revenue -> investment
// Incubation -> incubation
// Staff -> colaborators
// CompanyData -> revenue
// Partner -> partners
// DnaUspStamp -> dna_usp_stamp

@Serializable
enum class Step {
    CompanyData,
    Investment,
    Revenue,
    Incubation,
    DNAUspStamp,
    Staff,
    Partner,
    AboutCompany,
}
