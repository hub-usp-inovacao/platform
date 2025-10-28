package br.usp.inovacao.hubusp.config

data class JwtConfig(
    val secret: String,
    val issuer: String,
    val domain: String,
    val audience: String,
    val realm: String,
)
