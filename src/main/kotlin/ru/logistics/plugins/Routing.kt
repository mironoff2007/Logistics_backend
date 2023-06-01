package ru.logistics.plugins

import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import ru.logistics.security.authenticate
import ru.logistics.security.hashing.HashingService
import ru.logistics.security.signIn
import ru.logistics.security.signUp
import ru.logistics.security.token.TokenConfig
import ru.logistics.security.token.TokenService

fun Application.configureRouting(
    //userDataSource: UserDataSource,
    hashingService: HashingService,
    tokenService: TokenService,
    tokenConfig: TokenConfig
) {
    routing {
        signIn(//userDataSource,
            hashingService,
            tokenService,
            tokenConfig)
        signUp(
            hashingService,
            //userDataSource
        )
        authenticate()
        get("/") {
            call.respondText("Hello World!")
        }
    }
}
