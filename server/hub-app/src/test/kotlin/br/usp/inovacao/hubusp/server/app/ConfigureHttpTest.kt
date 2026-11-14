package br.usp.inovacao.hubusp.server.app

import io.ktor.server.testing.testApplication
import kotlin.test.Test

class ConfigureHttpTest {
    @Test
    fun `it configures CORS for wildcard host`() =
        testApplication {
            application {
                configureHttp()
            }
        }
}
