package br.usp.inovacao.hubusp.server.app.auth

import br.usp.inovacao.hubusp.config.Configuration
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import com.auth0.jwt.interfaces.Payload
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.auth.Principal
import io.ktor.server.auth.jwt.JWTChallengeContext
import io.ktor.server.auth.jwt.JWTCredential
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.response.respond

@kotlinx.serialization.Serializable
class CompanyJWT(val cnpj: String) : AuthProvider {
    override fun createToken(): String =
        JWT.create()
            .withAudience(Configuration.jwt.audience)
            .withIssuer(Configuration.jwt.issuer)
            .withClaim("cnpj", this.cnpj)
            .sign(Algorithm.HMAC256(Configuration.jwt.secret))

    companion object : StaticAuthProvider, StaticJWTAuthProvider, FromJWT<CompanyJWT> {
        override val providerName = "company-auth-jwt"

        override val realm = Configuration.jwt.realm

        override val verifier =
            JWT.require(Algorithm.HMAC256(Configuration.jwt.secret))
                .withAudience(Configuration.jwt.audience)
                .withIssuer(Configuration.jwt.issuer)
                .build()

        override val validate: suspend ApplicationCall.(JWTCredential) -> Principal? =
            { credential ->
                if (credential.payload.getClaim("cnpj").asString() != "") {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }

        override val challenge:
            suspend JWTChallengeContext.(defaultScheme: String, realm: String) -> Unit =
            { defaultScheme, _ ->
                call.respond(
                    HttpStatusCode.Unauthorized,
                    "Token for ${defaultScheme} is not valid or has expired",
                )
            }

        fun fromData(cnpj: String?): CompanyJWT? {
            return if (cnpj != null) {
                CompanyJWT(cnpj)
            } else {
                null
            }
        }

        override fun fromDecodedJWT(jwt: DecodedJWT): CompanyJWT? {
            val cnpj = jwt.getClaim("cnpj").asString()
            return fromData(cnpj)
        }

        override fun fromPayload(payload: Payload): CompanyJWT? {
            val cnpj = payload.getClaim("cnpj").asString()
            return fromData(cnpj)
        }
    }
}
