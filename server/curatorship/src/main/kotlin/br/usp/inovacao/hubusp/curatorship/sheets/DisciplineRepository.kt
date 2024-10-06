package br.usp.inovacao.hubusp.curatorship.sheets

interface DisciplineRepository {
    fun save(discipline:Discipline)
    fun clean()
}