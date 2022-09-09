package br.usp.inovacao.hubusp.server.cli

import br.usp.inovacao.hubusp.server.cli.commands.Test
import com.github.ajalt.clikt.core.subcommands

fun main(args: Array<String>) {
    CLI().run {
        subcommands(Test())
        main(args)
    }
}
