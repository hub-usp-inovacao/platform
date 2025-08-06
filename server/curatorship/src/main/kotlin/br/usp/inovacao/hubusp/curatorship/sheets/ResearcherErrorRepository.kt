package br.usp.inovacao.hubusp.curatorship.sheets

interface ResearcherErrorRepository {
    fun save(researcherError:Any)
    fun clean()
}
