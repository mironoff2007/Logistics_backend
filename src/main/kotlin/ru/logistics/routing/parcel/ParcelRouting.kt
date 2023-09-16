package ru.logistics.routing.parcel

import com.mironov.database.city.CityTable
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.logistics.routing.city.City
import ru.logistics.contract.parcel.SearchResponse
import ru.logistics.contract.parcel.ServerParcel

fun Application.parcelRouting() {
    routing {
        post("/registerParcels") {
            val request = call.receiveNullable<List<ServerParcel>>() ?: kotlin.run {
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

        get("/searchParcels") {
            try {
                val searchBy = call.parameters[SearchResponse.SEARCH_QUERY_TAG] ?: ""
                val fromCityId = call.parameters[SearchResponse.SEARCH_FROM_CITY_TAG] ?: ""
                val toCityId = call.parameters[SearchResponse.SEARCH_TO_CITY_TAG] ?: ""

                val cityFrom = CityTable.get(fromCityId)
                val cityTo = CityTable.get(toCityId)

                val parcels = ParcelsTable.selectSearch(
                    search = searchBy,
                    currentCitySearch = cityFrom,
                    destinationCitySearch = cityTo
                )
                val response = SearchResponse(parcels = parcels, page = 0)
                val json = Json.encodeToString<SearchResponse>(response)
                call.respond(json)
            } catch (e: Exception) {
                println(e.stackTraceToString())
                call.respond(HttpStatusCode.InternalServerError)
            }
        }
    }
}