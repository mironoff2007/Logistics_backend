package ru.logistics.security.data.responses

import kotlinx.serialization.Serializable
import ru.logistics.security.token.Token

@Serializable
data class AuthResponse(
    val token: Token
)
