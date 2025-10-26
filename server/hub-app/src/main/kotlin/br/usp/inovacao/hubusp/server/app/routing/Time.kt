package br.usp.inovacao.hubusp.server.app.routing

import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class Time {
    companion object {
        public fun timestamp() =
            ZonedDateTime.now(ZoneId.of("America/Sao_Paulo"))
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))
    }
}
