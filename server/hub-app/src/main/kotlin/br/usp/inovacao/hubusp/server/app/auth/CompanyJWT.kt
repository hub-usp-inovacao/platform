package br.usp.inovacao.hubusp.server.app.auth

import br.usp.inovacao.hubusp.config.Configuration
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import com.auth0.jwt.interfaces.Payload
import io.ktor.server.auth.jwt.jwt

@kotlinx.serialization.Serializable
class CompanyJWT(val cnpj: String) : AuthProvider {
    override fun createToken(): String =
        JWT.create()
            .withAudience(Configuration.jwt.audience)
            .withIssuer(Configuration.jwt.issuer)
            .withClaim("cnpj", this.cnpj)
            .sign(Algorithm.HMAC256(Configuration.jwt.secret))

    companion object : AuthProviderName, FromJWT<CompanyJWT> {
        override val providerName = "company-auth-jwt"

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
