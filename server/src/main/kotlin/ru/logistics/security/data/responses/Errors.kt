package ru.logistics.security.data.responses

import ru.mironov.logistics.ErrorResponse

enum class Errors(val code: Int, val msg: String) {
    WrongPasswordOrUser(1, "Incorrect username or password");

    fun toErrorResponse() = ErrorResponse(this.code, this.msg)
}