package ru.logistics

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.cio.*
import ru.logistics.city.cityRouting
import ru.logistics.plugins.*

fun main() {
    embeddedServer(CIO, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureSerialization()
    configureDatabases()
    configureSockets()
    configureHTTP()
    configureSecurity()
    configureRouting()
    cityRouting()
}
