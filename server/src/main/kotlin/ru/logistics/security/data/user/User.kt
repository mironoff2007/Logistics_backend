package ru.logistics.security.data.user

import java.util.Date

data class User(
    val id: Long = Date().time,
    val username: String,
    val password: String,
    val salt: String,
)