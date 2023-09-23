import ru.logistics.database.city.CityTable
import io.ktor.server.config.*
import io.ktor.server.testing.*
import ru.logistics.database.ParcelsTable
import ru.logistics.plugins.configureDatabasesTest
import ru.logistics.plugins.configureSerialization
import ru.mironov.logistics.parcel.ServerParcel
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
            CityTable.clear()
            CityTable.populateIfEmpty()

            val city = CityTable.fetchAll()[0]
            val parcelSave = ServerParcel(
                parcelId = 1L,
                customerName = "",
                customerSecondName = "",
                address = "",
                senderName = "",
                senderSecondName = "",
                senderAddress = "",
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

    @Test
    fun find_city_by_name_or_id_test() = testApplication {
        environment {
            config = MapApplicationConfig("ktor.environment" to "test")
        }
        application {
            configureSerialization()
            configureDatabasesTest()

            ParcelsTable.clear()
            CityTable.clear()
            CityTable.populateIfEmpty()

            val city1 = CityTable.get("1")
            val city2 = CityTable.get("Novgorod")
            val cityNull = CityTable.get("3")

            assert(city1?.id == 1)
            assert(city2?.id == 2)
            assert(cityNull?.id == null)
        }
    }
}
