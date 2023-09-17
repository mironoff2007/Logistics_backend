package ru.logistics.security.data.responses

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val code: Int,
    val msg: String,
)
