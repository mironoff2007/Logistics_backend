package ru.logistics.routing.city


import com.mironov.database.city.CityTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class CityEntity(id: EntityID<Int>): IntEntity(id) {
    var name by CityTable.cityName
    var sequelId by CityTable.id

    fun toCity(): City {
        return City(sequelId.value, name)
    }

    companion object: IntEntityClass<CityEntity>(CityTable)
}