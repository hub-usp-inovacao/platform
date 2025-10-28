package br.usp.inovacao.hubusp.config

import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.addResourceSource

data class RootConfig(
    val sheets: SheetsConfig,
    val email: EmailConfig,
    val ktor: KtorConfig,
    val jwt: JwtConfig,
    val database: DatabaseConfig,
)

val Configuration =
    ConfigLoaderBuilder.default()
        .addResourceSource("/application.conf", optional = true)
        .addResourceSource("/hub-usp.conf")
        .build()
        .configBinder()
        .bindOrThrow<RootConfig>("hub-usp")
