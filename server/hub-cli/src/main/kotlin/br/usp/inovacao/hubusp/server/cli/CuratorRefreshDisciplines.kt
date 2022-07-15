package br.usp.inovacao.hubusp.server.cli

import br.usp.inovacao.hubusp.server.curatorship.CurateDisciplines
import com.github.ajalt.clikt.core.CliktCommand

class CuratorRefreshDisciplines(
    private val curateDisciplines: CurateDisciplines
) : CliktCommand() {
    override fun run() {
        curateDisciplines.refreshDisciplines()
    }
}
