package br.usp.inovacao.hubusp.curatorship.sheets

import org.valiktor.ConstraintViolationException
import org.valiktor.functions.isNotNull
import org.valiktor.validate

data class Company(
    val cnpj: String?
) {
    companion object {
        fun fromRow(row: List<String?>) = Company(
            cnpj = row[1]
        )
    }

    init {
        try {
            validate(this) {
                validate(Company::cnpj)
                    .isNotNull()
                    .isCNPJ()
            }
        } catch (cve: ConstraintViolationException) {
            throw ValidationException.wrapping(cve)
        }
    }
}
