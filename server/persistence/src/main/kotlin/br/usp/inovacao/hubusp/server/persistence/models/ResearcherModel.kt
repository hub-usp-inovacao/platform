package br.usp.inovacao.hubusp.server.persistence.models

import br.usp.inovacao.hubusp.server.catalog.KnowledgeAreas
import kotlinx.serialization.Serializable

@Serializable
data class ResearcherModel(
    val name: String?,
    val email: String?,
    val unities: Set<String>?,
    val keywords: Set<String>?,
    val lattes: String?,
    val photo: String?,
    val skills: Set<String>?,
    val services: Set<String>?,
    val equipments: Set<String>?,
    val phone: String?,
    val limitDate: String?,
    val bond: String?,
    val campus: String?,
    val area: KnowledgeAreasModel
)

@Serializable
data class KnowledgeAreasModel(
    val area: Set<String>?,
    val subArea: Set<String>?
)