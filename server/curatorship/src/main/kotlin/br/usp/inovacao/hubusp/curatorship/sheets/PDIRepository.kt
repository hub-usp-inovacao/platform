package br.usp.inovacao.hubusp.curatorship.sheets

interface PDIRepository {
    fun save(pdi:PDI)
    fun clean()
}