package ru.logistics.database.user

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import ru.logistics.database.city.CityTable
import ru.logistics.security.data.user.User
import ru.mironov.logistics.UserRole

class UserEntity(id: EntityID<Int>): IntEntity(id) {
    var name by UserTable.name
    var sequelId by UserTable.userId
    var password by UserTable.passwordHash
    var salt by UserTable.salt
    var role by UserTable.role
    var loca by UserTable.location

    fun toUser(): User {
        val roleEnum = try {
            UserRole.valueOf(role)
        }
        catch (e: Exception) {
            println(e)
            UserRole.COURIER
        }
        return User(
            id = sequelId,
            username = name,
            password = password,
            salt = salt,
            role = roleEnum,
            location = CityTable.get(loca.value) ?: CityTable.initCities.first()
        )
    }

    companion object: IntEntityClass<UserEntity>(UserTable)
}