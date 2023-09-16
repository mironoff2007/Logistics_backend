package ru.logistics.contract.parcel

import kotlinx.serialization.Serializable

@Serializable
data class SearchResponse(
    val page: Int = 0,
    val parcels: List<ServerParcel>
) {
    companion object {
        const val SEARCH_QUERY_TAG = "searchBy"
        const val SEARCH_TO_CITY_TAG = "toCity"
        const val SEARCH_FROM_CITY_TAG = "fromCity"
    }
}