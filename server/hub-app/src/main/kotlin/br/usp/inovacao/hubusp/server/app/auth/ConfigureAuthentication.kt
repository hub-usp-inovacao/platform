package br.usp.inovacao.hubusp.server.app.auth

import br.usp.inovacao.hubusp.config.Configuration
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.Application
import io.ktor.server.auth.authentication
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt

fun Application.configureAuthentication() {
    val secret = Configuration.jwt.secret
    val issuer = Configuration.jwt.issuer
    val audience = Configuration.jwt.audience
    val myRealm = Configuration.jwt.realm

    authentication {
        jwt(HubJWT.AuthProvider.Company.toString()) {
            realm = myRealm

            verifier(
                JWT.require(Algorithm.HMAC256(secret))
                    .withAudience(audience)
                    .withIssuer(issuer)
                    .build(),
            )

            validate { credential ->
                if (credential.payload.getClaim("cnpj").asString() != "") {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
        }
    }
}
