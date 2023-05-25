package br.usp.inovacao.hubusp.configuration

import com.typesafe.config.ConfigFactory
import com.typesafe.config.Config

data class SheetConfig (
    val ID: String,
    val TAB_NAME: String
)

typealias ApiKey = String
typealias EmailAddress = String
typealias Password = String




@Suppress("unused")
object Configuration {
    private val appDotConf: Config = ConfigFactory.load()

    private fun read(path: String) = appDotConf.getString(path)

    object GoogleSheets {
        val API_KEY: ApiKey = read("sheets.api_key")
        val INCUBATORS = SheetConfig(
            ID = read("sheets.incubators_id"),
            TAB_NAME = read("sheets.incubators_tab")
        )
        val FUNDS = SheetConfig(
            ID = read("sheets.funds_id"),
            TAB_NAME = read("sheets.funds_tab")
        )
        val PDI = SheetConfig(
            ID = read("sheets.pdi_id"),
            TAB_NAME = read("sheets.pdi_tab")
        )
        val COMPANY = SheetConfig(
            ID = read("sheets.company_id"),
            TAB_NAME = read("sheets.company_tab")
        )
        val DISCIPLINE = SheetConfig(
            ID = read("sheets.discipline_id"),
            TAB_NAME = read("sheets.discipline_tab")
        )
        val PATENT = SheetConfig(
            ID = read("sheets.patent_id"),
            TAB_NAME = read("sheets.patent_tab")
        )
        val RESEARCHER = SheetConfig(
            ID = read("sheets.researcher_id"),
            TAB_NAME = read("sheets.researcher_tab")
        )
        val INITIATIVE = SheetConfig(
            ID = read("sheets.initiative_id"),
            TAB_NAME = read("sheets.initiative_tab")
        )

    }

    object Email {
        val DEVELOPERS: EmailAddress = read("email.devs")
        val USERNAME: EmailAddress = read("email.username")
        val PASSWORD: Password = read("email.password")
    }

    object Http {
        val PORT: String = read("ktor.deployment.port")
        val CORS_ALLOWED_HOSTS: String = read("ktor.deployment.allowedHosts")
    }

    object Security {
        val JWT_SECRET: String = read("jwt.secret")
        val ISSUER: String = read("jwt.issuer")
        val DOMAIN: String = read("jwt.domain")
        val AUDIENCE: String = read("jwt.audience")
        val REALM: String = read("jwt.realm")
    }

    object Persistence {
        val PROTOCOL: String = read("datasource.protocol")
        val HOST: String = read("datasource.host")
        val PORT: String = read("datasource.port")
        val DB_NAME: String = read("datasource.dbName")
    }

}
