package ru.logistics.security.data.responses

import kotlinx.serialization.Serializable
import ru.logistics.security.token.Token

@Serializable
data class ErrorResponse(
    val code: Int,
    val msg: String,
)
