package ru.logistics.plugins

import ru.logistics.database.city.CityTable
import org.jetbrains.exposed.sql.*
import io.ktor.server.application.*
import ru.logistics.database.ParcelsTable
import ru.logistics.database.user.UserTable

fun Application.configureDatabases() {
    val database = Database.connect(
        url = "jdbc:postgresql://localhost:5432/logistics",
        user = "logistics_admin",
        driver = "org.postgresql.Driver",
        password = "1234"
    )
    UserTable.initDb(database)
    ParcelsTable.initDb(database)
    CityTable.initDb(database)
    CityTable.populateIfEmpty()
}

fun Application.configureDatabasesTest() {
    val database = Database.connect(
        url = "jdbc:postgresql://localhost:5432/logistics_test",
        user = "logistics_admin",
        driver = "org.postgresql.Driver",
        password = "1234"
    )
    UserTable.initDb(database)
    ParcelsTable.initDb(database)
    CityTable.initDb(database)
}
