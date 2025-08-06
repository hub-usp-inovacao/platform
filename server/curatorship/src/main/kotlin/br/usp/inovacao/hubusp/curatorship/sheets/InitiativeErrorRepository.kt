package br.usp.inovacao.hubusp.curatorship.sheets

interface InitiativeErrorRepository {
    fun save(error: InitiativeValidationError)
    fun save(uniquenessError: InitiativeUniquenessError)
    fun clean()
}
