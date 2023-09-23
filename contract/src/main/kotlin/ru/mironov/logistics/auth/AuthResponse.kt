package ru.mironov.logistics.auth

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val token: ServerToken,
    val userRole: UserRole
)
