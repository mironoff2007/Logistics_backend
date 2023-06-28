package ru.logistics

import com.mironov.database.city.CityTable
import io.ktor.server.config.*
import io.ktor.server.testing.*
import ru.logistics.city.City
import ru.logistics.parcel.Parcel
import ru.logistics.parcel.ParcelsTable
import ru.logistics.plugins.configureDatabases
import ru.logistics.plugins.configureDatabasesTest
import ru.logistics.plugins.configureSerialization
import kotlin.test.Test

class DbTest {
    @Test
    fun testRoot() = testApplication {
        environment {
            config = MapApplicationConfig("ktor.environment" to "test")
        }
        application {
            configureSerialization()
            configureDatabasesTest()

            ParcelsTable.clear()
            val city = CityTable.fetchAll()[0]
            val parcelSave = Parcel(
                parcelId = 1L,
                customerName = "",
                customerSecondName = "",
                address = "",
                destinationCity = city,
                currentCity = city,
                senderCity = city,
                dateShow = "date",
                date = System.currentTimeMillis()
            )
            val parcels = listOf(parcelSave)

            ParcelsTable.insertAllBatch(parcels)
            val parcelResult = ParcelsTable.get(parcels[0].parcelId)
            assert(parcelResult == parcelSave)
        }
    }
}
