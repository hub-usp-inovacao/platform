package br.usp.inovacao.hubusp.server.app.routing

import br.usp.inovacao.hubusp.config.Configuration
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import java.util.Date

object TokenService {
    private val algorithm = Algorithm.HMAC256(Configuration.jwt.secret)

    fun create(payload: Map<String, String>): String {
        val builder =
            JWT.create()
                .withIssuer(Configuration.jwt.issuer)
                .withExpiresAt(Date(System.currentTimeMillis() + TOKEN_TTL_MS))

        payload.forEach { (key, value) -> builder.withClaim(key, value) }

        return builder.sign(algorithm)
    }

    fun verify(token: String): Map<String, String>? =
        try {
            val jwt =
                JWT.require(algorithm)
                    .withIssuer(Configuration.jwt.issuer)
                    .build()
                    .verify(token)

            jwt.claims
                .filterValues { !it.isNull }
                .mapValues { (_, claim) -> claim.asString() }
                .filterValues { it != null }
                .mapValues { (_, value) -> value!! }
        } catch (_: JWTVerificationException) {
            null
        }

    private const val TOKEN_TTL_MS = 24L * 60L * 60L * 1000L
}
