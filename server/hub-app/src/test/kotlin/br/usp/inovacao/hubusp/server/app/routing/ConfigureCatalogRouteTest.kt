package br.usp.inovacao.hubusp.server.app.routing

import br.usp.inovacao.hubusp.server.catalog.*
import br.usp.inovacao.hubusp.server.persistence.configureDB
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class ConfigureCatalogRouteTest {
    @Test
    fun `test GET company ecosystems`() = testCatalogApplication {
        // given ... when
        val response = client.get("/ecosystems")
        val body = response.bodyAsText().let {
            Json.decodeFromString<Map<String,List<String>>>(it)
        }

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(listOf("ecosystems"), body.keys.toList())
    }

    @Test
    fun `test GET company cities`() = testCatalogApplication {
        // given ... when
        val response = client.get("/company_cities")
        val body = response.bodyAsText().let {
            Json.decodeFromString<Map<String,List<String>>>(it)
        }

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(listOf("cities"), body.keys.toList())
    }

    @Test
    fun `test GET patent classifications`() = testCatalogApplication {
        // given ... when
        val response = client.get("/patent_classifications")
        val body = response.bodyAsText().let {
            Json.decodeFromString<Map<String,List<String>>>(it)
        }

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(listOf("classifications"), body.keys.toList())
    }

    @Test
    fun `test GET pdis`() = testCatalogApplication {
        // given ... when
        val response = client.get("/pdis")
        val body = response.bodyAsText().let {
            Json.decodeFromString<Map<String, List<PDI>>>(it)
        }

        // then
        assertEquals(HttpStatusCode.OK, response.status)
        assertContentEquals(listOf("pdis"), body.keys.toList())
    }

    @Test
    fun `test GET disciplines`() = testCatalogApplication {
        // given ... when
        val response = client.get("/disciplines")
        val body = response.bodyAsText().let {
            Json.decodeFromString<Map<String, List<Discipline>>>(it)
        }

        // then
        assertEquals(HttpStatusCode.OK, response.status)
        assertContentEquals(listOf("disciplines"), body.keys.toList())
    }

    @Test
    fun `test GET skills`() = testCatalogApplication {
        // given ... when
        val response = client.get("/skills")
        val body = response.bodyAsText().let {
            Json.decodeFromString<Map<String, List<Researcher>>>(it)
        }

        // then
        assertEquals(HttpStatusCode.OK, response.status)
        assertContentEquals(listOf("skills"), body.keys.toList())
    }

    @Test
    fun `test GET companies`() = testCatalogApplication {
        // given ... when
        val response = client.get("/companies")
        val body = response.bodyAsText().let {
            Json.decodeFromString<Map<String, List<Company>>>(it)
        }

        // then
        assertEquals(HttpStatusCode.OK, response.status)
        assertContentEquals(listOf("companies"), body.keys.toList())
    }

    @Test
    fun `test GET patents`() = testCatalogApplication {
        // given ... when
        val response = client.get("/patents")
        val body = response.bodyAsText().let {
            Json.decodeFromString<Map<String, List<Patent>>>(it)
        }

        // then
        assertEquals(HttpStatusCode.OK, response.status)
        assertContentEquals(listOf("patents"), body.keys.toList())
    }

    @Test
    fun `test GET initiatives`() = testCatalogApplication {
        // given ... when
        val response = client.get("/initiatives")
        val body = response.bodyAsText().let {
            Json.decodeFromString<Map<String, List<Initiative>>>(it)
        }

        // then
        assertEquals(HttpStatusCode.OK, response.status)
        assertContentEquals(listOf("initiatives"), body.keys.toList())
    }

    private fun testCatalogApplication(
        block: suspend ApplicationTestBuilder.() -> Unit
    ) = testApplication {
        application {
            val db = configureDB(
                protocol = environment.config.property("datasource.protocol").getString(),
                host = environment.config.property("datasource.host").getString(),
                port = environment.config.property("datasource.port").getString(),
                dbName = environment.config.property("datasource.dbName").getString()
            )
            configureCatalogRoute(db)
        }

        block()
    }
}
