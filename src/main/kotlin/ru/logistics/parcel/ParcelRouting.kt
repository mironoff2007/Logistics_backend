package ru.logistics.parcel

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun Application.parcelRouting() {
    routing {
        post("/registerParcels") {
            val request = call.receiveNullable<List<Parcel>>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

            try {
                ParcelsTable.insertAllBatch(request)
                call.respond(HttpStatusCode.OK)
            }
            catch (e: Exception)
            {
                println(e.stackTraceToString())
                call.respond(HttpStatusCode.InternalServerError)
            }

        }
    }
}