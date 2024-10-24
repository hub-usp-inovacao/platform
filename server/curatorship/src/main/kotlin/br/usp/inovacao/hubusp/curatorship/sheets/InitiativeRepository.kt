package br.usp.inovacao.hubusp.curatorship.sheets

interface InitiativeRepository {
    fun save(initiative: Initiative)
    fun clean()
}
