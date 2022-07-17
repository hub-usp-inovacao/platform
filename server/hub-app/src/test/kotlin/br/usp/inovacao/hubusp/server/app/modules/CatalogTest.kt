package br.usp.inovacao.hubusp.server.app.modules

import br.usp.inovacao.hubusp.server.catalog.Company
import br.usp.inovacao.hubusp.server.catalog.Discipline
import br.usp.inovacao.hubusp.server.catalog.Patent
import br.usp.inovacao.hubusp.server.catalog.Researcher
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

class CatalogTest {
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

    private fun testCatalogApplication(
        block: suspend ApplicationTestBuilder.() -> Unit
    ) = testApplication {
        application {
            catalog()
        }

        block()
    }
}
