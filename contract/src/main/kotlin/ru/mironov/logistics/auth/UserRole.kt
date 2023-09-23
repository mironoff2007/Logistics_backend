package ru.mironov.logistics.auth

import kotlinx.serialization.Serializable
import ru.mironov.logistics.ServerCity

@Serializable
data class UserRole(
    val id: Int,
    val location: ServerCity
)