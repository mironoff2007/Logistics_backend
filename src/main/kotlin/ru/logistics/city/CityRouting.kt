package ru.logistics.city

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private val initCities = listOf(
    City(1, "Moscow"),
    City(2, "Novgorod")
)

fun Application.cityRouting() {
    routing {
        get("/cities") {
            val json = Json.encodeToString<List<City>>(initCities)
            call.respondText(json)
        }
    }
}