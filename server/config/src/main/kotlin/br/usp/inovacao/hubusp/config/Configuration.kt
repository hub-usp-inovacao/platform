package br.usp.inovacao.hubusp.config

import com.typesafe.config.ConfigBeanFactory
import com.typesafe.config.ConfigFactory

data class RootConfig(
    var sheets: SheetsConfig = SheetsConfig(),
    // TODO:
    // - email
    // - ktor
    // - jwt
    // - datasource
)

val Configuration: RootConfig by lazy {
    ConfigBeanFactory.create(ConfigFactory.load().getConfig("hub-usp"), RootConfig::class.java)
}
