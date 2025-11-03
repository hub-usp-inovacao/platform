package br.usp.inovacao.hubusp.server.app.auth

import com.auth0.jwt.interfaces.DecodedJWT
import com.auth0.jwt.interfaces.Payload

interface AuthProvider {
    abstract fun createToken(): String
}

interface AuthProviderName {
    abstract val providerName: String
}

interface FromJWT<out Self> {
    abstract fun fromDecodedJWT(jwt: DecodedJWT): Self?

    abstract fun fromPayload(payload: Payload): Self?
}
