package ru.logistics.routing

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.logistics.database.city.CityTable
import ru.mironov.logistics.ServerCity

fun Application.cityRouting() {
    routing {
        get("/cities") {
            val cities = CityTable.fetchAll()
            val json = Json.encodeToString<List<ServerCity>>(cities)
            call.respondText(json)
        }
    }
}