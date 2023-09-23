package ru.mironov.logistics

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val code: Int,
    val msg: String,
)
