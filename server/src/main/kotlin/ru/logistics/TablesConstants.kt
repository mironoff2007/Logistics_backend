package ru.logistics

object TablesConstants {

    const val PARCELS_TABLE_NAME = "parcels_table"
    const val CITIES_TABLE_NAME = "cities_table"
    const val USERS_TABLE_NAME = "users_table"

    fun selectCountQuery(tableName: String) = "SELECT COUNT (*) FROM $tableName"

}