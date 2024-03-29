package ru.logistics.database.city


import ru.mironov.logistics.ServerCity
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class CityEntity(id: EntityID<Int>): IntEntity(id) {
    private var name by CityTable.cityName
    private var sequelId by CityTable.id

    fun toCity(): ServerCity {
        return ServerCity(sequelId.value, name)
    }

    companion object: IntEntityClass<CityEntity>(CityTable)
}