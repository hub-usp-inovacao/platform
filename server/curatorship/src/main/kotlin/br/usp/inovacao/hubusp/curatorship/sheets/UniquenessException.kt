package br.usp.inovacao.hubusp.curatorship.sheets

class UniquenessException(
    override val message: String
) : RuntimeException(message)
