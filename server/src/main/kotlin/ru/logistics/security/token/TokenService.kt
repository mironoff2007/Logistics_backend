package ru.logistics.security.token

import ru.logistics.security.token.Token
import ru.logistics.security.token.TokenClaim
import ru.logistics.security.token.TokenConfig

interface TokenService {
    fun generate(
        config: TokenConfig,
        vararg claims: TokenClaim
    ): Token
}