package ru.logistics.routing.city

import com.mironov.database.city.CityTable
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun Application.cityRouting() {
    routing {
        get("/cities") {
            val cities = CityTable.fetchAll()
            val json = Json.encodeToString<List<City>>(cities)
            call.respondText(json)
        }
    }
}