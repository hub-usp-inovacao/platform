package br.usp.inovacao.hubusp.server.cli

import br.usp.inovacao.hubusp.server.catalog.DisciplineRepository
import com.github.ajalt.clikt.core.CliktCommand

class LogDisciplines(
    private val repository: DisciplineRepository
) : CliktCommand() {
    override fun run() {
        val none = emptyMap<String, List<String>>()

        repository
            .filter(none)
            .forEach { println(it) }
    }
}
