package ru.logistics.routing.city

import ru.mironov.logistics.ServerCity
import ru.logistics.TablesConstants.CITIES_TABLE_NAME
import ru.logistics.TablesConstants.selectCountQuery
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object CityTable : IntIdTable(CITIES_TABLE_NAME) {

    private val initCities = listOf(
        ServerCity(1, "Moscow"),
        ServerCity(2, "Novgorod")
    )

    val cityName = CityTable.varchar(name = "name", length = 50)

    fun initDb(database: Database) {
        transaction(database) {
            SchemaUtils.createMissingTablesAndColumns(this@CityTable)
        }
    }

    fun populateIfEmpty() {
        transaction {
            if(fetchAll().isEmpty()) insertAll(initCities)
        }
    }

    @Throws
    fun clear() {
        transaction {
            CityTable.deleteAll()
        }
    }

    @Throws
    fun replaceAll(cities: List<ServerCity>) {
        transaction {
            CityTable.batchReplace(cities) {
                this[CityTable.id] = it.id
                this[cityName] = it.name
            }
        }
    }

    @Throws
    fun count(): Int {
        var count = 0
        transaction {
            exec(selectCountQuery(CITIES_TABLE_NAME)) { res ->
                res.next()
                count = res.getInt(1)
            }
        }
        return count
    }

    private fun fromRow(row: ResultRow): CityEntity {
        val i = row[id]
        return CityEntity[i]
    }

    @Throws
    fun fetchAll(): List<ServerCity> {
        return try {
            transaction {
                CityTable.selectAll().toList()
                    .map {
                        fromRow(it).toCity()
                    }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun get(id: Int): ServerCity? {
        return try {
            transaction {
                CityTable.select {
                    CityTable.id eq id
                }.limit(1).single().let { fromRow(it).toCity() }
            }
        } catch (e: Exception) {
            null
        }
    }

    fun get(idOrName: String): ServerCity? {
        return try {
            transaction {
                CityTable.select {
                    (CityTable.id eq idOrName.toIntOrNull()) or
                    (cityName eq idOrName)
                }.limit(1).single().let { fromRow(it).toCity() }
            }
        } catch (e: Exception) {
            null
        }
    }

    fun insertAll(cities: List<ServerCity>) {
        transaction {
            CityTable.batchInsert (cities){
                this[CityTable.id] = it.id
                this[cityName] = it.name
            }
        }
    }

}