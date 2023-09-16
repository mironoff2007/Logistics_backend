package ru.logistics

import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import ru.logistics.routing.city.cityRouting
import ru.logistics.routing.parcel.parcelRouting
import ru.logistics.plugins.*
import ru.logistics.security.data.user.UserTable
import ru.logistics.security.hashing.SHA256HashingService
import ru.logistics.security.token.JwtTokenService
import ru.logistics.security.token.TokenConfig

fun Application.module() {

    val tokenService = JwtTokenService()
    var secret = System.getenv("JWT_SECRET")
    if (secret == null) {
        println("!!!SECRET IS MISSED, USE DEFAULT, THIS IS OK ONLY FOR TESTING")
        secret = "test_secret"
    }
    val tokenConfig = TokenConfig(
        issuer = environment.config.property("jwt.issuer").getString(),
        audience = environment.config.property("jwt.audience").getString(),
        expiresIn = 365L * 1000L * 60L * 60L * 24L,
        secret = secret

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
    parcelRouting()
    cityRouting()
}
