package br.usp.inovacao.hubusp.server.cli.commands

import br.usp.inovacao.hubusp.curatorship.CliCommandTest
import com.github.ajalt.clikt.core.CliktCommand

class Test :
    CliktCommand(
        help = "Works as a tracer-bullet and manual test for the integration of CLI to Core") {
    override fun run() = CliCommandTest().execute()
}
