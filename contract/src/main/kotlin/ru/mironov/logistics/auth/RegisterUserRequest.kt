package ru.mironov.logistics.auth

import kotlinx.serialization.Serializable

@Serializable
data class RegisterUserRequest(
    val username: String,
    val password: String,
    val location: String,
    val role: String,
)
