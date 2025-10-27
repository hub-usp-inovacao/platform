package br.usp.inovacao.hubusp.curatorship.companyform

import br.usp.inovacao.hubusp.curatorship.companyform.step.Step

data class CompanyFormValidationException(val errorsPerStep: ErrorsPerStep) :
    RuntimeException(errorsPerStep.toList().joinToString("|"))

typealias ErrorsPerStep = Map<Step, List<String>>
