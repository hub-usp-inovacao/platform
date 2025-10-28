package br.usp.inovacao.hubusp.config

data class DatabaseConfig(
    val protocol: String,
    val host: String,
    val port: String,
    val dbName: String,
)
