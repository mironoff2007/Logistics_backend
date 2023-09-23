package ru.logistics.security.token

import ru.mironov.logistics.auth.ServerToken

interface TokenService {
    fun generate(
        config: TokenConfig,
        vararg claims: TokenClaim
    ): ServerToken
}