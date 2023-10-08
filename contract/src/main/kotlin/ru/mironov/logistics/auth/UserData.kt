package ru.mironov.logistics.auth

import kotlinx.serialization.Serializable
import ru.mironov.logistics.ServerCity
import ru.mironov.logistics.UserRole

@Serializable
data class UserData(
    val userId: Long,
    val userStoreId: Long,
    val location: ServerCity,
    val role: UserRole,
)