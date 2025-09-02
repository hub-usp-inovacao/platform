package br.usp.inovacao.hubusp.curatorship.register

import org.valiktor.Validator
import org.valiktor.constraints.In
import org.valiktor.constraints.Website

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
