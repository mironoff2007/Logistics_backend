package ru.logistics.security

import ru.mironov.logistics.auth.AuthRequest
import ru.logistics.security.data.user.User
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.apache.commons.codec.digest.DigestUtils
import ru.logistics.database.city.CityTable
import ru.logistics.security.data.responses.Errors
import ru.logistics.database.user.UserTable
import ru.logistics.security.hashing.HashingService
import ru.logistics.security.hashing.SaltedHash
import ru.logistics.security.token.TokenClaim
import ru.logistics.security.token.TokenConfig
import ru.logistics.security.token.TokenService
import ru.mironov.logistics.UserRole
import ru.mironov.logistics.auth.AuthResponse
import ru.mironov.logistics.auth.RegisterUserRequest
import ru.mironov.logistics.auth.UserData

fun Route.signUp(
    hashingService: HashingService,
) {
    post("signup") {
        val request = call.receiveNullable<RegisterUserRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        val areFieldsBlank = request.username.isBlank() || request.password.isBlank()
        val isPwTooShort = request.password.length < 4
        val city = CityTable.get(request.location)
        val role = UserRole.valueOf(request.role)
        if (areFieldsBlank || isPwTooShort) {
            call.respond(HttpStatusCode.Conflict, "password is too short")
            return@post
        }

        val userExists = UserTable.getByName(request.username) != null
        if (userExists) {
            call.respond(HttpStatusCode.Conflict, "username ${request.username} already exists")
            return@post
        }

        val saltedHash = hashingService.generateSaltedHash(request.password)
        val user = User(
            username = request.username,
            password = saltedHash.hash,
            salt = saltedHash.salt,
            role = role, location = city ?: CityTable.initCities.first()
        )
        UserTable.insert(user)

        call.respond(HttpStatusCode.OK)
    }
}

fun Route.signIn(
    hashingService: HashingService,
    tokenService: TokenService,
    tokenConfig: TokenConfig
) {
    post("signin") {
        val request = call.receiveOrNull<AuthRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        val user = UserTable.getByName(request.username)
        if (user == null) {
            call.respond(HttpStatusCode.Conflict, Errors.WrongPasswordOrUser.toErrorResponse())
            return@post
        }

        val isValidPassword = hashingService.verify(
            value = request.password,
            saltedHash = SaltedHash(
                hash = user.password,
                salt = user.salt
            )
        )
        if (!isValidPassword) {
            println("Entered hash: ${DigestUtils.sha256Hex("${user.salt}${request.password}")}, Hashed PW: ${user.password}")
            call.respond(HttpStatusCode.Conflict,  Errors.WrongPasswordOrUser.toErrorResponse())
            return@post
        }

        val token = tokenService.generate(
            config = tokenConfig,
            TokenClaim(
                name = "userId",
                value = user.id.toString()
            )
        )

        val userData = UserData(location = user.location, role = user.role)

        call.respond(
            status = HttpStatusCode.OK,
            message = AuthResponse(token, userData)
        )
    }
}

fun Route.authenticate() {
    authenticate {
        get("authenticate") {
            call.respond(HttpStatusCode.OK)
        }
    }
}

fun Route.getSecretInfo() {
    authenticate {
        get("secret") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.getClaim("userId", String::class)
            call.respond(HttpStatusCode.OK, "Your userId is $userId")
        }
    }
}