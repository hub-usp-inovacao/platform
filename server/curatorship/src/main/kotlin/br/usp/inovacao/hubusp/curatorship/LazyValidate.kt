package br.usp.inovacao.hubusp.curatorship

/**
 * Allows lazy validations
 *
 * Useful to control when the validation happens instead of forcing it on init.
 */
interface LazyValidate {
    fun validate()
}
