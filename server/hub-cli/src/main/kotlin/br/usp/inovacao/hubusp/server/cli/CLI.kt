package br.usp.inovacao.hubusp.server.cli

import com.github.ajalt.clikt.core.CliktCommand

class CLI : CliktCommand(help = "The Hub CLI tool to assist automation of platform's tasks") {
    override fun run() {
        println(this.shortHelp())
    }
}
