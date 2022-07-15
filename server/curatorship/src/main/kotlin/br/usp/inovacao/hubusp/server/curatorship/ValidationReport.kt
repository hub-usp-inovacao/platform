package br.usp.inovacao.hubusp.server.curatorship

@kotlinx.serialization.Serializable
class ValidationReport<T> private constructor(
    val entity: T,
    val approved: Boolean = true,
    val errors: List<String> = emptyList()
) {
    companion object {
        fun <T> withError(entity: T, errors: List<String>) = ValidationReport(
            entity,
            approved = false,
            errors = errors
        )

        fun <T> approved(entity: T) = ValidationReport(
            entity,
            approved = true
        )
    }
}
