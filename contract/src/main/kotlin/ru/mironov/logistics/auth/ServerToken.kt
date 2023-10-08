package ru.mironov.logistics.auth

import kotlinx.serialization.Serializable

@Serializable
data class ServerToken(
    val value: String,
    val expireAt: Long
) {
    override fun toString(): String {
        return "token expire at $expireAt"
    }
}