package ru.mironov.logistics.auth

import kotlinx.serialization.Serializable

@Serializable
data class RegisterUserRequest(
    val username: String,
    val userStoreId: Long,
    val password: String,
    val location: String,
    val role: String,
)
