package br.usp.inovacao.hubusp.server.app.modules

import br.usp.inovacao.hubusp.server.catalog.Discipline
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
    fun testGetDisciplines() = testCatalogApplication {
        // given ... when
        val response = client.get("/disciplines")
        val body = response.bodyAsText().let {
            Json.decodeFromString<Map<String, List<Discipline>>>(it)
        }

        // then
        assertContentEquals(listOf("disciplines"), body.keys.toList())
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun testGetResearchers() = testCatalogApplication {
        // given ... when
        val response = client.get("/skills")
        val body = response.bodyAsText().let {
            Json.decodeFromString<Map<String, List<Researcher>>>(it)
        }

        // then
        assertContentEquals(listOf("skills"), body.keys.toList())
        assertEquals(HttpStatusCode.OK, response.status)
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
