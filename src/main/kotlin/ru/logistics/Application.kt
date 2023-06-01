package ru.logistics

import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import ru.logistics.city.cityRouting
import ru.logistics.plugins.*
import ru.logistics.security.data.user.UserTable
import ru.logistics.security.hashing.SHA256HashingService
import ru.logistics.security.token.JwtTokenService
import ru.logistics.security.token.TokenConfig

fun Application.module() {

    val tokenService = JwtTokenService()
    val tokenConfig = TokenConfig(
        issuer = environment.config.property("jwt.issuer").getString(),
        audience = environment.config.property("jwt.audience").getString(),
        expiresIn = 365L * 1000L * 60L * 60L * 24L,
        secret = System.getenv("JWT_SECRET")
    )
    val hashingService = SHA256HashingService()
    configureSerialization()
    configureDatabases()
    configureSockets()
    configureHTTP()
    configureSecurity(tokenConfig)
    configureRouting(
        hashingService = hashingService,
        tokenService = tokenService,
        tokenConfig = tokenConfig
    )
    cityRouting()
}
