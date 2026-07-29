package br.usp.inovacao.hubusp.server.app.routing

import br.usp.inovacao.hubusp.config.Configuration
import br.usp.inovacao.hubusp.curatorship.sheets.RefreshCompany
import br.usp.inovacao.hubusp.curatorship.sheets.RefreshDiscipline
import br.usp.inovacao.hubusp.curatorship.sheets.RefreshInitiative
import br.usp.inovacao.hubusp.curatorship.sheets.RefreshPDI
import br.usp.inovacao.hubusp.curatorship.sheets.RefreshPatent
import br.usp.inovacao.hubusp.curatorship.sheets.RefreshResearcher
import br.usp.inovacao.hubusp.server.app.configureSerialization
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.unmockkAll
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ConfigureRefreshRouteTest {
    @AfterTest
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `it rejects refresh when secret is missing or wrong`() =
        testApplication {
            application {
                configureRefreshRoute(mockk())
                configureSerialization()
            }

            val response = client.post("/refresh")

            assertEquals(HttpStatusCode.Unauthorized, response.status)
        }

    @Test
    fun `it accepts refresh when secret matches`() =
        testApplication {
            mockkConstructor(RefreshCompany::class)
            mockkConstructor(RefreshPDI::class)
            mockkConstructor(RefreshDiscipline::class)
            mockkConstructor(RefreshResearcher::class)
            mockkConstructor(RefreshInitiative::class)
            mockkConstructor(RefreshPatent::class)

            every { anyConstructed<RefreshCompany>().refresh() } returns Unit
            every { anyConstructed<RefreshPDI>().refresh() } returns Unit
            every { anyConstructed<RefreshDiscipline>().refresh() } returns Unit
            every { anyConstructed<RefreshResearcher>().refresh() } returns Unit
            every { anyConstructed<RefreshInitiative>().refresh() } returns Unit
            every { anyConstructed<RefreshPatent>().refresh() } returns Unit

            environment {
                config =
                    io.ktor.server.config.MapApplicationConfig(
                        "ktor.environment" to "dev",
                    )
            }

            application {
                configureRefreshRoute(mockk())
                configureSerialization()
            }

            val response = client.post("/refresh") {
                header("X-Refresh-Secret", "test-secret")
            }

            assertEquals(HttpStatusCode.Unauthorized, response.status)
        }
}
