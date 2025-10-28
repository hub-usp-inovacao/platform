package br.usp.inovacao.hubusp.config

data class EmailConfig(
    val devs: String,
    val cc: Set<String>,
    val username: String,
    val password: String,
)
