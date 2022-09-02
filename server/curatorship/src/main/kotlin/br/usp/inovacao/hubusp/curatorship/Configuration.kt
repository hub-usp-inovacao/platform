package br.usp.inovacao.hubusp.curatorship

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory

object Configuration {
    private val loader: Config = ConfigFactory.load()

    val SHEETS_API_KEY = loader.getString("curatorship.sheets.api_key")

    val PDI_SHEET_ID = loader.getString("curatorship.sheets.pdi_id")
    val PDI_TAB_NAME = loader.getString("curatorship.sheets.pdi_tab")

    val COMPANY_SHEET_ID = loader.getString("curatorship.sheets.company_id")
    val COMPANY_TAB_NAME = loader.getString("curatorship.sheets.company_tab")

    val DISCIPLINE_SHEET_ID = loader.getString("curatorship.sheets.discipline_id")
    val DISCIPLINE_TAB_NAME = loader.getString("curatorship.sheets.discipline_tab")

    val PATENT_SHEET_ID = loader.getString("curatorship.sheets.patent_id")
    val PATENT_TAB_NAME = loader.getString("curatorship.sheets.patent_tab")

    val RESEARCHER_SHEET_ID = loader.getString("curatorship.sheets.researcher_id")
    val RESEARCHER_TAB_NAME = loader.getString("curatorship.sheets.researcher_tab")

    val INITIATIVE_SHEET_ID = loader.getString("curatorship.sheets.initiative_id")
    val INITIATIVE_TAB_NAME = loader.getString("curatorship.sheets.initiative_tab")
}