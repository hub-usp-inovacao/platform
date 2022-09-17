package br.usp.inovacao.hubusp.curatorship.sheets

class ValidationException(
    val messages: Iterable<String>
) : RuntimeException(messages.joinToString("|"))