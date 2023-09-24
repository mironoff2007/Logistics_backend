package ru.logistics.security.data.user

import ru.mironov.logistics.ServerCity
import ru.mironov.logistics.UserRole
import java.util.Date

data class User(
    val id: Long = Date().time,
    val username: String,
    val password: String,
    val salt: String,
    val role: UserRole,
    val location: ServerCity,
)