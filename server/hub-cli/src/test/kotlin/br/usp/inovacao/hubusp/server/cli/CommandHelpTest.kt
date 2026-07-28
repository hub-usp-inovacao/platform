package br.usp.inovacao.hubusp.server.cli

import br.usp.inovacao.hubusp.server.cli.commands.MigrateTextIndexes
import com.github.ajalt.clikt.core.ProgramResult
import kotlin.test.Test
import kotlin.test.assertFailsWith

class CommandHelpTest {
    @Test
    fun `it prints help for migrate text indexes`() {
        assertFailsWith<ProgramResult> {
            MigrateTextIndexes().main(arrayOf("--help"))
        }
    }

    @Test
    fun `it prints help for the tracer bullet command`() {
        assertFailsWith<ProgramResult> {
            br.usp.inovacao.hubusp.server.cli.commands.Test().main(arrayOf("--help"))
        }
    }
}
