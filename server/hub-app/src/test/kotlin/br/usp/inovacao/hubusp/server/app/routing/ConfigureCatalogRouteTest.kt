package br.usp.inovacao.hubusp.server.app.routing

import br.usp.inovacao.hubusp.config.Configuration
import br.usp.inovacao.hubusp.server.app.configureSerialization
import br.usp.inovacao.hubusp.server.catalog.*
import br.usp.inovacao.hubusp.server.persistence.configureDB
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class ConfigureCatalogRouteTest {
    @Test
    fun `test GET company ecosystems`() = testCatalogApplication {
        val response = client.get("/ecosystems")

        val body: Map<String, List<String>> = response.body()

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(listOf("ecosystems"), body.keys.toList())
    }

    @Test
    fun `test GET company cities`() = testCatalogApplication {
        val response = client.get("/company_cities")

        val body: Map<String, List<String>> = response.body()

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(listOf("cities"), body.keys.toList())
    }

    @Test
    fun `test GET patent classifications`() = testCatalogApplication {
        val response = client.get("/patent_classifications")

        val body: Map<String, List<String>> = response.body()

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(listOf("classifications"), body.keys.toList())
    }

    @Test
    fun `test GET pdis`() = testCatalogApplication {
        val response = client.get("/pdis")

        val body: Map<String, List<PDI>> = response.body()

        assertEquals(HttpStatusCode.OK, response.status)
        assertContentEquals(listOf("pdis"), body.keys.toList())
    }

    @Test
    fun `test GET disciplines`() = testCatalogApplication {
        val response = client.get("/disciplines")

        val body: Map<String, List<Discipline>> = response.body()

        assertEquals(HttpStatusCode.OK, response.status)
        assertContentEquals(listOf("disciplines"), body.keys.toList())
    }

    @Test
    fun `test GET skills`() = testCatalogApplication {
        val response = client.get("/skills")

        val body: Map<String, List<Researcher>> = response.body()

        assertEquals(HttpStatusCode.OK, response.status)
        assertContentEquals(listOf("skills"), body.keys.toList())
    }

    @Test
    fun `test GET companies`() = testCatalogApplication {
        val response = client.get("/companies")

        val body: Map<String, List<Company>> = response.body()

        assertEquals(HttpStatusCode.OK, response.status)
        assertContentEquals(listOf("companies"), body.keys.toList())
    }

    @Test
    fun `test GET patents`() = testCatalogApplication {
        val response = client.get("/patents")

        val body: Map<String, List<Patent>> = response.body()

        assertEquals(HttpStatusCode.OK, response.status)
        assertContentEquals(listOf("patents"), body.keys.toList())
    }

    @Test
    fun `test GET initiatives`() = testCatalogApplication {
        val response = client.get("/initiatives")

        val body: Map<String, List<Initiative>> = response.body()

        assertEquals(HttpStatusCode.OK, response.status)
        assertContentEquals(listOf("initiatives"), body.keys.toList())
    }

    private fun testCatalogApplication(block: suspend ApplicationTestBuilder.() -> Unit) =
        testApplication {
            application {
                val db =
                    configureDB(
                        protocol = Configuration.database.protocol,
                        host = Configuration.database.host,
                        port = Configuration.database.port,
                        dbName = Configuration.database.dbName,
                    )
                configureCatalogRoute(db)
                configureSerialization()
            }

            client = createClient { install(ContentNegotiation) { json() } }

            block()
        }
}
