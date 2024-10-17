package br.usp.inovacao.hubusp.server.cli

import br.usp.inovacao.hubusp.server.cli.commands.Test
import br.usp.inovacao.hubusp.server.cli.commands.curatorship.RefreshPDI
import br.usp.inovacao.hubusp.server.cli.commands.curatorship.RefreshCompany
import br.usp.inovacao.hubusp.server.cli.commands.curatorship.RefreshDiscipline
import com.github.ajalt.clikt.core.subcommands

fun main(args: Array<String>) {
    CLI().run {
        subcommands(Test(), RefreshPDI(), RefreshCompany(), RefreshDiscipline())
        main(args)
    }
}
