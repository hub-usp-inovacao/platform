package br.usp.inovacao.hubusp.server.app

import br.usp.inovacao.hubusp.config.Configuration
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm

/* imports originais
import io.ktor.server.application.Application
import io.ktor.server.auth.authentication
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt
*/

import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

@Suppress("unused")
fun Application.configureSecurity() {
    val secret = Configuration.jwt.secret
    val issuer = Configuration.jwt.issuer
    val audience = Configuration.jwt.audience
    val myRealm = Configuration.jwt.realm

    // isso esta no server/config/src/main/kotlin/br/usp/inovacao/hubusp/config
    val uspClientId = Configuration.usp.clientId
    val uspClientSecret = Configuration.usp.clientSecret

    authentication {
        jwt {
            realm = myRealm

            verifier(
                JWT.require(Algorithm.HMAC256(secret))
                    .withAudience(audience)
                    .withIssuer(issuer)
                    .build(),
            )

            validate { credential ->
                if (credential.payload.getClaim("username").asString() != "") {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
        }

        oauth("usp-oauth") {
            // URL de retorno que a USP vai chamar
            // por enquanto fica o localhost msm
            urlProvider = { "http://localhost:8080/callback" }
            
            providerLookup = {
                OAuthServerSettings.OAuth1ServerSettings(
                    name = "usp",
                    //authorizeUrl = "https://uspdigital.usp.br/wsusuario/oauth/authorize",
                    //accessTokenUrl = "https://uspdigital.usp.br/wsusuario/oauth/token",
                    authorizeUrl = "http://localhost:5259/wsusuario/oauth/authorize",
                    accessTokenUrl = "http://localhost:5259/wsusuario/oauth/request_token",
                    requestMethod = HttpMethod.Post,
                    clientId = uspClientId,
                    clientSecret = uspClientSecret,
                    defaultScopes = listOf("user")
                )
            }
            
            client = HttpClient(Apache)
        }
    }
}
