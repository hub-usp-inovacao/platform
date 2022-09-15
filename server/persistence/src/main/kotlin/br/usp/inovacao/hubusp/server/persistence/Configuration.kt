package br.usp.inovacao.hubusp.server.persistence

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory

object Configuration {
    private val loader: Config = ConfigFactory.load()

    val SHEETS_API_KEY = loader.getString("persistence.sheets.api_key")

    val INCUBATOR_ID = loader.getString("persistence.sheets.incubators_id")
    val INCUBATOR_TAB = loader.getString("persistence.sheets.incubators_tab")

    val FUNDS_ID = loader.getString("persistence.sheets.funds_id")
    val FUNDS_TAB = loader.getString("persistence.sheets.funds_tab")
}