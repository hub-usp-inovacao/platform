package br.usp.inovacao.hubusp.curatorship.sheets

interface PDIErrorRepository {
    fun save(pdiError:ValidationError)
}