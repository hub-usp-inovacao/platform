package br.usp.inovacao.hubusp.server.persistence

fun connectToTestDb() = configureDB(
    protocol = System.getenv("HUB_TEST_DATASOURCE_PROTOCOL") ?: "mongodb",
    host = System.getenv("HUB_TEST_DATASOURCE_HOST") ?: "localhost",
    port = System.getenv("HUB_TEST_DATASOURCE_PORT") ?: "27017",
    dbName = System.getenv("HUB_TEST_DATASOURCE_DBNAME") ?: "test_database"
)
