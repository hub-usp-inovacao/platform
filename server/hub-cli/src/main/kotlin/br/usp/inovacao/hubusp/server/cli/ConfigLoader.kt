package br.usp.inovacao.hubusp.server.cli

object ConfigLoader {
    val SHEETS_API_KEY: String = System.getenv("HUB_SHEETS_API_KEY") ?: ""

    val DATASOURCE_PROTOCOL: String = System.getenv("HUB_DATASOURCE_PROTOCOL") ?: ""
    val DATASOURCE_HOST: String = System.getenv("HUB_DATASOURCE_HOST") ?: ""
    val DATASOURCE_PORT: String = System.getenv("HUB_DATASOURCE_PORT") ?: ""
    val DATASOURCE_DBNAME: String = System.getenv("HUB_DATASOURCE_DBNAME") ?: ""
}
