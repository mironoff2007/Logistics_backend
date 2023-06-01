package ru.logistics.plugins

import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.coroutines.*
import java.sql.*
import org.jetbrains.exposed.sql.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import ru.logistics.security.data.user.UserTable

fun Application.configureDatabases() {
    val database = Database.connect(
        url = "jdbc:postgresql://localhost:5432/logistics",
        user = "logistics_admin",
        driver = "org.postgresql.Driver",
        password = "1234"
    )
    UserTable.initDb(database)
}
