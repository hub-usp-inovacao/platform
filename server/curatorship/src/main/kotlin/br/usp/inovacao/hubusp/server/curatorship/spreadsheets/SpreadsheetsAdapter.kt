package br.usp.inovacao.hubusp.server.curatorship.spreadsheets

import br.usp.inovacao.hubusp.server.curatorship.Discipline

interface SpreadsheetsAdapter {
    fun fetchDisciplines(): Iterable<Discipline>
}
