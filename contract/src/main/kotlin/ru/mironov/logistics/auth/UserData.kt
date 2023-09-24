package ru.mironov.logistics.auth

import kotlinx.serialization.Serializable
import ru.mironov.logistics.ServerCity
import ru.mironov.logistics.UserRole

@Serializable
data class UserData(
    val location: ServerCity,
    var role: UserRole
)