package ru.logistics.city


import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class CityEntity(id: EntityID<Int>): IntEntity(id) {
    var name by CityTable.cityName
    var sequelId by CityTable.cityId

    fun toCity(): City {
        return City(sequelId, name)
    }

    companion object: IntEntityClass<CityEntity>(CityTable)
}