package br.usp.inovacao.hubusp.curatorship.sheets

interface PatentRepository {
    fun save(patent:Patent)
    fun clean()
}