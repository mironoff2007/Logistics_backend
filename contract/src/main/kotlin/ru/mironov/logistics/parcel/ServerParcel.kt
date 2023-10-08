package ru.mironov.logistics.parcel

import kotlinx.serialization.Serializable
import ru.mironov.logistics.ServerCity

@Serializable
data class ServerParcel(
    val parcelId: Long,
    val storeId: Long,
    val customerName: String,
    val customerSecondName: String,
    val address: String,
    val senderName: String,
    val senderSecondName: String,
    val senderAddress: String,
    val destinationCity: ServerCity,
    val currentCity: ServerCity,
    val senderCity: ServerCity,
    val dateShow: String,
    val date: Long
)