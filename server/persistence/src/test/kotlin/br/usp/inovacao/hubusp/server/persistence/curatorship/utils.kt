package br.usp.inovacao.hubusp.server.persistence.curatorship

import br.usp.inovacao.hubusp.server.persistence.configureDB
import java.util.UUID
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.utility.DockerImageName

private val mongoContainer by lazy {
    MongoDBContainer(DockerImageName.parse("mongo:7.0.14"))
        .withReuse(false)
        .apply { start() }
}

fun connectToTestDb() = configureDB(
    protocol = "mongodb",
    host = mongoContainer.host,
    port = mongoContainer.firstMappedPort.toString(),
    dbName = System.getenv("HUB_TEST_DATASOURCE_DBNAME")
        ?: "test_database_${UUID.randomUUID().toString().replace("-", "")}"
)
