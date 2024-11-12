package br.usp.inovacao.hubusp.curatorship.sheets

interface ResearcherRepository {
    fun save(researcher:Researcher)
    fun clean()
}
