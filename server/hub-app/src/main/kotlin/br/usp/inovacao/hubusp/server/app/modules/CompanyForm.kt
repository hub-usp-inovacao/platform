package br.usp.inovacao.hubusp.server.app.models

import kotlinx.serialization.Serializable

@Serializable
data class CompanyForm(
    val name: String,
    val corporateName: String,
    val cnpj: String,
    val year: String,
    val cnae: String,
    val address: Address,
    val phones: List<String>,
    val emails: List<String>,
    val description: String,
    val services: List<String>,
    val technologies: List<String>,
    val logo: String? = null,
    val url: String? = null,
    val incubated: String,
    val ecosystems: List<String>,
    val companySize: List<String>,
    val partners: List<PartnerForm>? = null
)

@Serializable
data class Address(
    val venue: String,
    val neighborhood: String,
    val city: String,
    val state: String,
    val cep: String
)

@Serializable
data class PartnerForm(
    val name: String,
    val nusp: String? = null,
    val bond: String? = null,
    val unity: String? = null,
    val email: String? = null,
    val phone: String? = null
)