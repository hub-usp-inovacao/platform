package br.usp.inovacao.hubusp.curatorship.sheets

import org.valiktor.Constraint
import org.valiktor.Validator

class PDICategory: Constraint

fun <E> Validator<E>.Property<String?>.isPDICategory() = this.validate(PDICategory()) {
    it == null || PDI.categories
        .contains(it)
}

class Phone : Constraint

fun <E> Validator<E>.Property<String?>.isPhone() = this.validate(Phone()) {
    (it == null) || it
        .replace("""\D""".toRegex(), "")
        .matches("""^\d{8,13}$""".toRegex())
}

class Campi : Constraint

fun <E> Validator<E>.Property<String?>.isCampus() = this.validate(Campi()) {
    (it == null) || Campus
        .all()
        .contains(it)
}

class Unity : Constraint

fun <E> Validator<E>.Property<String?>.isUnity() = this.validate(Unity()) {
    it == null || it in Campus.allUnities()
}

class CNPJ : Constraint {
    companion object {
        fun isFormatValid(input: String): Boolean {
            val cnpjRegex = """^\d{2}\.\d{3}\.\d{3}/\d{4}-\d{2}$""".toRegex()
            return input.matches(cnpjRegex)
        }
    }
}

fun <E> Validator<E>.Property<String?>.isCNPJ() = this.validate(CNPJ()) {
    it == null || CNPJ.isFormatValid(it)
}