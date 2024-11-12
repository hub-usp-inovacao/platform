package br.usp.inovacao.hubusp.server.persistence.models

import kotlinx.serialization.Serializable

@Serializable
data class InitiativeModel(
    val classification: String,
    val contact: InitiativeContact,
    val description: String,
    val email: Set<String>? = null,
    val localization: String,
    val name: String,
    val tags: Set<String>,
    val unity: String,
    val url: String? = null,
)

@Serializable
data class InitiativeContact(
    val info: String,
    val person: String
)