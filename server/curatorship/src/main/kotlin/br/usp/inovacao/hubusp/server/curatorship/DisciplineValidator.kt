package br.usp.inovacao.hubusp.server.curatorship

class DisciplineValidator {

    companion object {
        val UNITIES = listOf("IME")
    }

    fun validate(discipline: Discipline): ValidationReport<Discipline> {
        val validName = discipline.name.length in 7..100
        val validUnity = discipline.unity in UNITIES

        var hasError = false
        val errors = mutableListOf<String>()

        if (!validName) {
            hasError = true
            errors.add("Nome: precisa ter entre 7 e 100 caractéres")
        }

        if (!validUnity) {
            hasError = true
            errors.add("Unidade: precisa estar na lista de unidades da Universidade")
        }

        if (hasError) return ValidationReport.withError(discipline, errors)

        return ValidationReport.approved(discipline)
    }
}
