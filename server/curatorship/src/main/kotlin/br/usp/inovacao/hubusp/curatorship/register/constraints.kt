package br.usp.inovacao.hubusp.curatorship.register

import org.valiktor.Constraint
import org.valiktor.Validator
import org.valiktor.constraints.In
import org.valiktor.constraints.Website

// TODO: Move this up a module, since it's generic

fun <E> Validator<E>.Property<Iterable<String>?>.isWebsite() =
    this.validate(Website) {
        it == null ||
            it.all {
                it.matches(
                    Regex(
                        "^(https?:\\/\\/)?([a-zA-Z0-9]+(-?[a-zA-Z0-9])*\\.)+[\\w]{2,}(\\/\\S*)?\$"))
            }
    }

fun <E, T> Validator<E>.Property<Iterable<T>?>.isIn(values: Iterable<T>) =
    this.validate(In(values)) { it == null || it.all { values.contains(it) } }

object Cnpj : Constraint

/** Validates if the [String] property is a valid CNPJ */
fun <E> Validator<E>.Property<String?>.isCnpj() =
    this.validate(Cnpj) {
        it == null ||
            it.matches(Regex("^[0-9]{2}.[0-9]{3}.[0-9]{3}/[0-9]{4}-[0-9]{2}$")) ||
            it.matches(Regex("^Exterior[0-9]*$"))
    }

object Cnae : Constraint

/** Validates if the [String] property is a valid CNAE (7 numbers) */
fun <E> Validator<E>.Property<String?>.isCnae(): Validator<E>.Property<String?> =
    this.validate(Cnae) {
        it == null || it.replace(Regex("[^0-9]"), "").matches(Regex("^[0-9]{7}$"))
    }

class Phone : Constraint

/** Validates if the [Iterable<String>] property are valid phone numbers */
fun <E> Validator<E>.Property<Iterable<String>?>.isPhone() =
    this.validate(Phone()) {
        (it == null) || it.all { it.replace(Regex("[^0-9]"), "").matches(Regex("^[0-9]{8,13}$")) }
    }

class Cep : Constraint

/** Validates if the [String] property is a valid CEP */
fun <E> Validator<E>.Property<String?>.isCep() =
    this.validate(Cep()) {
        it == null || it.replace(Regex("[^0-9]"), "").matches(Regex("^[0-9]{8}$"))
    }

class CompanyNature : Constraint

/**
 * Validates if the [String] property is a valid CompanyNature
 *
 * See: https://concla.ibge.gov.br/images/concla/documentacao/CONCLA-TNJ2021-EstruturaDetalhada.pdf
 */
fun <E> Validator<E>.Property<String?>.isCompanyNature() =
    this.validate(CompanyNature()) { it == null || it.matches(Regex("^[0-9]{3}-[0-9].+$")) }

class Number : Constraint

/** Validates if the [String] property is a valid Number */
fun <E> Validator<E>.Property<String?>.isNumber() =
    this.validate(Number()) { it == null || it.matches(Regex("^[0-9]+$")) }
