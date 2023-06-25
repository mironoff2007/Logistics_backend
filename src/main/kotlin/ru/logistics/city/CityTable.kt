package ru.logistics.city

import com.mironov.database.TablesConstants.CITIES_TABLE_NAME
import com.mironov.database.TablesConstants.selectCountQuery
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object CityTable : IntIdTable(CITIES_TABLE_NAME) {

    val cityId = CityTable.integer("city_id").uniqueIndex()
    val cityName = CityTable.varchar(name = "name", length = 50)

    fun initDb(database: Database) {
        transaction(database) {
            SchemaUtils.createMissingTablesAndColumns(this@CityTable)
        }
    }

    @Throws
    fun clear() {
        transaction {
            CityTable.deleteAll()
        }
    }

    @Throws
    fun replace(cityEntity: CityEntity) {
        transaction {
            CityTable.replace {
                it[cityId] = cityEntity.sequelId
                it[cityName] = cityEntity.name
            }
        }
    }

    @Throws
    fun replaceAll(cities: List<City>) {
        transaction {
            CityTable.batchReplace(cities) {
                this[cityId] = it.id
                this[cityName] = it.name
            }
        }
    }

    @Throws
    fun replaceAllTransaction(parcels: List<CityEntity>) {
        transaction {
            parcels.forEach { city ->
                replace(city)
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
    fun fetchAll(): List<City> {
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

    @Throws
    fun get(id: Int): City? {
        return try {
            transaction {
                CityTable.select {
                    cityId eq id
                }.limit(1).single().let { fromRow(it).toCity() }
            }
        } catch (e: Exception) {
            null
        }
    }

}