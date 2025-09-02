package br.usp.inovacao.hubusp.curatorship.register

import br.usp.inovacao.hubusp.curatorship.register.step.Step

data class CompanyStepValidationException(val errorsPerStep: Map<Step, List<String>>) :
    RuntimeException(errorsPerStep.toList().joinToString("|"))
