package br.usp.inovacao.hubusp.server.app.routing

object RoutingException {
    class BadRequest(val response: Any) : RuntimeException()

    class NotFound(val response: Any) : RuntimeException()

    class Unprocessable(val response: Any) : RuntimeException()
}
