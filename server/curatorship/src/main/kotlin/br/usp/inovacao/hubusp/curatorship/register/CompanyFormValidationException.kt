package br.usp.inovacao.hubusp.curatorship.register

import br.usp.inovacao.hubusp.curatorship.register.step.Step

data class CompanyFormValidationException(val errorsPerStep: ErrorsPerStep) :
    RuntimeException(errorsPerStep.toList().joinToString("|"))

typealias ErrorsPerStep = Map<Step, List<String>>
