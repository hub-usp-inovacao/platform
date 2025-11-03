package br.usp.inovacao.hubusp.server.app.auth

import br.usp.inovacao.hubusp.config.Configuration
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.auth.jwt.jwt

/** Wrapper around JWT methods */
class HubJWT {
    companion object {
        /** Create token for AuthProvider.Company */
        fun createCompanyToken(cnpj: String): String {
            return JWT.create()
                .withAudience(Configuration.jwt.audience)
                .withIssuer(Configuration.jwt.issuer)
                .withClaim("cnpj", cnpj)
                .sign(Algorithm.HMAC256(Configuration.jwt.secret))
        }
    }

    enum class AuthProvider {
        Company
    }
}
