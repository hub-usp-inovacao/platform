package br.usp.inovacao.hubusp.server.app.routing

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureAuthRouting() {
    routing {
        // o usuário clica e vai pra USP
        authenticate("usp-oauth") {
            get("/login") {
                // redireciona automaticamente pra usp
            }

            // a usp devolve o usuário com o token
            get("/callback") {
                val principal = call.authentication.principal<OAuthAccessTokenResponse.OAuth2>()

                if (principal != null) {
                    // por enquanto, só avisando que deu certo
                    // o próximo passo seria pegar o nome e nº usp usando o principal.accessToken
                    call.respondText("Login realizado com sucesso! Token: ${principal.accessToken}")
                } else {
                    call.respondText("Erro: Não foi possível obter os dados da USP.")
                }
            }
        }
    }
}