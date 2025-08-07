package br.usp.inovacao.hubusp.server.persistence.models

import kotlinx.serialization.Serializable

@Serializable
data class CompanyModel(
    val active: Boolean? = true,
    val address: CompanyAddressModel,
    val allowed: Boolean? = true,
    val classification: CompanyClassificationModel,
    val cnae: String,
    val cnpj: String,
    val companySize: Set<String>,
    val corporateName: String? = "",
    val description: String,
    val ecosystems: Set<String>,
    val emails: Set<String>,
    val incubated: String,
    val logo: String? = null,
    val name: String,
    val phones: Set<String>,
    val services: Set<String>,
    val technologies: Set<String>,
    val partners: List<PartnerModel>,
    val url: String? = null,
    val year: String
)
@Serializable
data class PartnerModel(
    val name: String,
    val nusp: String,
    val bond: String,
    val unity: String,
    val email: String,
    val phone: String,
)

@kotlinx.serialization.Serializable
data class CompanyClassificationModel(
    val major: String,
    val minor: String
)

@kotlinx.serialization.Serializable
data class CompanyAddressModel(
    val cep: String,
    val city: String,
    val neighborhood: String,
    val state: String,
    val venue: String
)
