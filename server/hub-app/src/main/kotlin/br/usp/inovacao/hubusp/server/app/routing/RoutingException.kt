package br.usp.inovacao.hubusp.server.app.routing

object RoutingException {
    class BadRequestException(
        message: String?,
    ) : RuntimeException(message)

    class NotFoundException(
        message: String?,
    ) : RuntimeException(message)
}
