package br.usp.inovacao.hubusp.server.curatorship

@kotlinx.serialization.Serializable
data class Discipline(
    val name: String,
    val unity: String
)
