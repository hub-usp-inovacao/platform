package br.usp.inovacao.hubusp.curatorship.register

class ValidationException (
    val messages: List<String>
): RuntimeException(messages.joinToString { "|" })