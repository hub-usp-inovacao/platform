package br.usp.inovacao.hubusp.server.cli

import br.usp.inovacao.hubusp.server.cli.commands.Test
import br.usp.inovacao.hubusp.server.cli.commands.curatorship.RefreshPDI
import com.github.ajalt.clikt.core.subcommands

fun main(args: Array<String>) {
    CLI().run {
        subcommands(Test(), RefreshPDI())
        main(args)
    }
}
