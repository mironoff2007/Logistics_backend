package ru.logistics.security.token

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.*

class JwtTokenService : TokenService {

    override fun generate(config: TokenConfig, vararg claims: TokenClaim): Token {
        val expireDate = Date(System.currentTimeMillis() + config.expiresIn)
        var token = JWT.create()
            .withAudience(config.audience)
            .withIssuer(config.issuer)
            .withExpiresAt(expireDate)
        claims.forEach { claim ->
            token = token.withClaim(claim.name, claim.value)
        }
        return Token(
            value = token.sign(Algorithm.HMAC256(config.secret)),
            expireAt = expireDate.time
        )
    }
}