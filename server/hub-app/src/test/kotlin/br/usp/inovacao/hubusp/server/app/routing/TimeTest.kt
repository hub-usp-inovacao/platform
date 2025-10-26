package br.usp.inovacao.hubusp.server.app.routing

import kotlin.test.Test

class TimeTest {
    @Test
    fun `timestamp does_not_throw_an_error`() {
        Time.timestamp()
    }
}
