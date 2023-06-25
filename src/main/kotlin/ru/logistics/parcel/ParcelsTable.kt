package ru.logistics.parcel

import com.mironov.database.TablesConstants.PARCELS_TABLE_NAME
import com.mironov.database.TablesConstants.selectCountQuery
import ru.logistics.andIf
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import ru.logistics.city.City
import ru.logistics.city.CityTable

object ParcelsTable : Table(PARCELS_TABLE_NAME) {

    private val parcelId = ParcelsTable.long("parcel_id").uniqueIndex()
    private val customerName = ParcelsTable.varchar(name = "customer_name", length = 50)
    private val customerSecondName = ParcelsTable.varchar(name = "customer_second_name", length = 50)
    private val address = ParcelsTable.varchar(name = "customer_address", length = 150)
    private val senderName = ParcelsTable.varchar(name = "sender_name", length = 50)
    private val senderSecondName = ParcelsTable.varchar(name = "sender_second_name", length = 50)
    private val senderAddress = ParcelsTable.varchar(name = "sender_address", length = 150)
    private val destinationCity = ParcelsTable.reference("destination_city", CityTable)
    private val senderCity = ParcelsTable.reference("sender_city", CityTable)
    private val currentCity = ParcelsTable.reference("current_city", CityTable)
    private val dateShow = ParcelsTable.varchar(name = "date_show", length = 50)
    private val date = ParcelsTable.long(name = "date")

    @Throws
    fun initDb(database: Database) {
        transaction(database) {
            SchemaUtils.createMissingTablesAndColumns(this@ParcelsTable)
        }
    }

    @Throws
    fun clear() {
        transaction {
            ParcelsTable.deleteAll()
        }
    }

    @Throws
    fun replace(parcel: Parcel) {
        transaction {
            ParcelsTable.replace {
                it[parcelId] = parcel.parcelId
                it[customerName] = parcel.customerName
                it[customerSecondName] = parcel.customerSecondName
                it[address] = parcel.address
                it[senderName] = parcel.senderName
                it[senderSecondName] = parcel.senderSecondName
                it[senderAddress] = parcel.senderAddress
                it[date] = parcel.date
                it[dateShow] = parcel.dateShow
                it[destinationCity] = parcel.destinationCity.id
                it[senderCity] = parcel.senderCity.id
                it[currentCity] = parcel.currentCity.id
            }
        }
    }

    @Throws
    fun inTransaction(method: () -> Unit) {
        transaction {
            method.invoke()
        }
    }

    @Throws
    fun insertAllBatch(parcels: List<Parcel>) {
        transaction {
            ParcelsTable.batchInsert(parcels) {
                this[parcelId] = it.parcelId
                this[customerName] = it.customerName
                this[customerSecondName] = it.customerSecondName
                this[address] = it.address
                this[senderName] = it.senderName
                this[senderSecondName] = it.senderSecondName
                this[senderAddress] = it.senderAddress
                this[date] = it.date
                this[dateShow] = it.dateShow
                this[destinationCity] = it.destinationCity.id
                this[currentCity] = it.currentCity.id
                this[senderCity] = it.senderCity.id
            }
        }
    }

    @Throws
    fun replaceAllTransaction(parcels: List<Parcel>) {
        transaction {
            parcels.forEach { parcel ->
                replace(parcel)
            }
        }
    }

    @Throws
    fun count(): Int {
        var count = 0
        transaction {
            exec(selectCountQuery(PARCELS_TABLE_NAME)) { res ->
                res.next()
                count = res.getInt(1)
            }
        }
        return count
    }

    @Throws
    private fun fromRow(row: ResultRow): Parcel {
        val destinationCityId = row[destinationCity].value
        val currentCityId = row[currentCity].value
        val senderCityId = row[senderCity].value
        return Parcel(
            parcelId = row[parcelId],
            customerName = row[customerName],
            customerSecondName = row[customerSecondName],
            address = row[address],
            senderName= row [senderAddress],
            senderSecondName = row [senderSecondName],
            senderAddress = row[senderAddress],
            dateShow = row[dateShow],
            date = row[date],
            destinationCity = CityTable.get(destinationCityId)!!,
            currentCity = CityTable.get(currentCityId)!!,
            senderCity = CityTable.get(senderCityId)!!
        )
    }

    @Throws
    fun fetchAll(): List<Parcel> {
        return transaction {
            ParcelsTable.selectAll().toList().map { fromRow(it) }
        }
    }

    @Throws
    fun get(id: Long): Parcel? {
        return transaction {
            ParcelsTable.select { parcelId eq id }
                .limit(1).single().let { fromRow(it) }
        }
    }

    @Throws
    fun selectBetweenDate(start: Long, end: Long): List<Parcel> {
        return transaction {
            val list = ParcelsTable.select() { (date greater start) and (date lessEq end) }.toList()
            list.map { fromRow(it) }
        }
    }

    @Throws
    fun selectSearch(
        search: String,
        currentCitySearch: City?,
        destinationCitySearch: City?
    ): List<Parcel> {
        val numb = search.toLongOrNull() ?: 0
        val length = search.length
        var divBy = 1L
        repeat(length) {
            divBy *= 10
        }
        return transaction {
            val list = ParcelsTable.select() {
                val exp = Op.build {
                            (customerName like "%$search%") or
                            (customerSecondName like "%$search%") or
                            (address like "%$search%") or
                            ((parcelId minus numb) mod divBy eq 0)
                }.andIf(destinationCitySearch != null) {
                    (destinationCity eq destinationCitySearch?.id)
                }.andIf(currentCitySearch != null) {
                    (currentCity eq currentCitySearch?.id)
                }
                exp
            }.toList()
            list.map { fromRow(it) }
        }
    }


}







