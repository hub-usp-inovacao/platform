package br.usp.inovacao.hubusp.curatorship.sheets

interface PatentErrorRepository {
    fun save(patentError:Any)
    fun clean()
}