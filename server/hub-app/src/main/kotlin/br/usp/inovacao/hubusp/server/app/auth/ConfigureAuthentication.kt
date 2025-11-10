package br.usp.inovacao.hubusp.server.app.auth

import io.ktor.server.application.Application
import io.ktor.server.auth.authentication
import io.ktor.server.auth.jwt.jwt

fun Application.configureAuthentication() {
    authentication {
        jwt(CompanyJWT.providerName) {
            realm = CompanyJWT.realm
            verifier(CompanyJWT.verifier)
            validate(CompanyJWT.validate)
            challenge(CompanyJWT.challenge)
        }
    }
}
