package br.usp.inovacao.hubusp.curatorship.register.step

class StepValidationException(val messages: List<String>) :
    RuntimeException(messages.joinToString("|"))
