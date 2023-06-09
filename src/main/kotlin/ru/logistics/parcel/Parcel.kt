package ru.logistics.parcel

import kotlinx.serialization.Serializable
import ru.logistics.city.City
import java.util.Date

@Serializable
data class Parcel(
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
    val dateShow: String = Date().toString(),
    val date: Long = Date().time
)