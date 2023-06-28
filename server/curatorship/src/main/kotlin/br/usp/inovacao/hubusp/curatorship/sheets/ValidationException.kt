package br.usp.inovacao.hubusp.curatorship.sheets

class ValidationException(
    val messages: List<String>
) : RuntimeException(messages.joinToString("|"))