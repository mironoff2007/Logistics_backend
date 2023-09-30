import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.server.testing.*
import kotlin.test.*
import io.ktor.http.*
import io.ktor.server.config.*

class ApplicationSampleTest {
    @Test
    fun testRoot() = testApplication {
        environment {
            config = ApplicationConfig(Config.TEST_CONFIG)
        }
        application {

        }
        client.get("/cities").apply {
            assertEquals(HttpStatusCode.OK, status)
            println(bodyAsText())
        }
    }
}
