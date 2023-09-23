import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.server.testing.*
import kotlin.test.*
import io.ktor.http.*
import io.ktor.server.config.*
import ru.logistics.routing.cityRouting
import ru.logistics.routing.parcelRouting
import ru.logistics.plugins.*
import ru.logistics.security.hashing.SHA256HashingService
import ru.logistics.security.token.JwtTokenService
import ru.logistics.security.token.TokenConfig

//todo fix
class ApplicationTest {
    @Test
    fun testRoot() = testApplication {
        environment {
            config = MapApplicationConfig("ktor.environment" to "test")
        }
        application {
            val tokenService = JwtTokenService()
            var secret = System.getenv("JWT_SECRET")
            if (secret == null) {
                println("!!!SECRET IS MISSED, USE DEFAULT, THIS IS OK ONLY FOR TESTING")
                secret = "test_secret"
            }
            val tokenConfig = TokenConfig(
                issuer = "test_issuer",
                audience = "test_audience",
                expiresIn = 10L * 1000L,
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
        client.get("/").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("Hello World!", bodyAsText())
        }
    }
}
