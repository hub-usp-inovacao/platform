package br.usp.inovacao.hubusp.server.persistence.models

import br.usp.inovacao.hubusp.server.catalog.Classification
import kotlinx.serialization.Serializable

@Serializable
data class CompanyModel(
    val address: CompanyAddress,
    val classification: CompanyClassification,
    val cnae: String,
    val companySize: Set<String>,
    val description: String,
    val ecosystems: Set<String>,
    val emails: Set<String>,
    val incubated: String,
    val logo: String? = null,
    val name: String,
    val phones: Set<String>,
    val services: Set<String>,
    val technologies: Set<String>,
    val partners: Set<Partner>,
    val url: String? = null
)
@Serializable
data class Partner(
    val name: String,
    val nusp: String,
    val bond: String,
    val unity: String,
    val email: String,
    val phone: String,
)

@kotlinx.serialization.Serializable
data class CompanyClassification(
    val major: String,
    val minor: String
)

@kotlinx.serialization.Serializable
data class CompanyAddress(
    val cep: String,
    val city: Set<String>,
    val neighborhood: String,
    val state: String,
    val venue: String
)