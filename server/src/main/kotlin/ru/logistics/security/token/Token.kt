package ru.logistics.security.token

import kotlinx.serialization.Serializable

@Serializable
data class Token(
    val value: String,
    val expireAt: Long
)