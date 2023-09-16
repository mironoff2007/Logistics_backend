package ru.logistics.contract.parcel

import kotlinx.serialization.Serializable
import ru.logistics.routing.city.City

@Serializable
data class ServerParcel(
    val parcelId: Long,
    val customerName: String,
    val customerSecondName: String,
    val address: String,
    val senderName: String = "",
    val senderSecondName: String = "",
    val senderAddress: String = "",
    val destinationCity: City,
    val currentCity: City,
    val senderCity: City,
    val dateShow: String,
    val date: Long
)