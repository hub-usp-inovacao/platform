package br.usp.inovacao.hubusp.server.cli.commands

import br.usp.inovacao.hubusp.server.app.auth.CompanyJWT
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.arguments.argument

class CompanyForm : CliktCommand() {
    class Jwt : CliktCommand() {
        val cnpj by argument()

        override fun run() {
            echo(CompanyJWT(cnpj).createToken())
        }
    }

    init {
        subcommands(Jwt())
    }

    override fun run() {}
}
