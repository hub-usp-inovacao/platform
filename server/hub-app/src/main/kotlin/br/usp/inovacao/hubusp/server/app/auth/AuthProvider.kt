package br.usp.inovacao.hubusp.server.app.auth

import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.interfaces.DecodedJWT
import com.auth0.jwt.interfaces.Payload
import io.ktor.server.application.ApplicationCall
import io.ktor.server.auth.Principal
import io.ktor.server.auth.jwt.JWTChallengeContext
import io.ktor.server.auth.jwt.JWTCredential

interface AuthProvider {
    abstract fun createToken(): String
}

interface StaticAuthProvider {
    abstract val providerName: String
}

interface StaticJWTAuthProvider {
    abstract val realm: String
    abstract val verifier: JWTVerifier
    abstract val validate: suspend ApplicationCall.(JWTCredential) -> Principal?
    abstract val challenge:
        suspend JWTChallengeContext.(defaultScheme: String, realm: String) -> Unit
}

interface FromJWT<out Self> {
    abstract fun fromDecodedJWT(jwt: DecodedJWT): Self?

    abstract fun fromPayload(payload: Payload): Self?
}
