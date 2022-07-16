package br.usp.inovacao.hubusp.server.app.modules

import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import kotlin.test.Test
import kotlin.test.assertEquals

class CatalogTest {
    @Test
    fun testGetDisciplines() = testCatalogApplication {
        val response = client.get("/disciplines")

        println(response.bodyAsText())
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
