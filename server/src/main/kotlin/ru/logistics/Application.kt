package ru.logistics

import io.ktor.server.application.*
import ru.logistics.plugins.*
import ru.logistics.routing.cityRouting
import ru.logistics.routing.parcelRouting
import ru.logistics.security.hashing.SHA256HashingService
import ru.logistics.security.token.JwtTokenService
import ru.logistics.security.token.TokenConfig

fun Application.module() {

    val tokenService = JwtTokenService()
    val secret = environment.config.property("jwt.secret").getString()
    val tokenConfig = TokenConfig(
        issuer = environment.config.property("jwt.issuer").getString(),
        audience = environment.config.property("jwt.audience").getString(),
        expiresIn = environment.config.property("jwt.timeout").getString().toLong(),
        secret = secret
    )
    val hashingService = SHA256HashingService()

    configureSerialization()
    configureHTTP()
    configureSockets()
    configureSecurity(tokenConfig)
    configureRouting(
        hashingService = hashingService,
        tokenService = tokenService,
        tokenConfig = tokenConfig
    )
    parcelRouting()
    cityRouting()

    if (isTest()) {
        configureDatabasesTest()
    } else {
        configureDatabases()
    }
}

fun Application.isTest() = try {
    environment.config.property("test").getString() == "true"
} catch (e: Exception) {
    false
}

