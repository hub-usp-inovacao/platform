package br.usp.inovacao.hubusp.server.curatorship.spreadsheets

import br.usp.inovacao.hubusp.server.curatorship.Discipline

class SpreadsheetsAdapterImpl(
    private val apiKey: String
) : SpreadsheetsAdapter {
    override fun fetchDisciplines(): Iterable<Discipline> {
        println(apiKey)
        return emptyList()
    }
}
