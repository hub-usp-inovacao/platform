package br.usp.inovacao.hubusp.server.cli

import kotlin.test.Test

class CLITest {
    @Test
    fun `it prints help when run`() {
        val output = java.io.ByteArrayOutputStream()
        val original = System.out
        try {
            System.setOut(java.io.PrintStream(output))
            CLI().run()
        } finally {
            System.setOut(original)
        }

        check(output.toString().contains("Hub CLI tool"))
    }
}
