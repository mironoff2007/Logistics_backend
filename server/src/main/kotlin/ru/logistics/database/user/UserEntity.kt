package ru.logistics.database.user

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import ru.logistics.database.city.CityTable
import ru.logistics.security.data.user.User
import ru.mironov.logistics.UserRole

class UserEntity(id: EntityID<Int>): IntEntity(id) {
    private var name by UserTable.name
    private var userStoreId by UserTable.userStoreId
    private var sequelId by UserTable.userId
    private var password by UserTable.passwordHash
    private var salt by UserTable.salt
    private var role by UserTable.role
    private var location by UserTable.location

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
            userStoreId = userStoreId,
            username = name,
            password = password,
            salt = salt,
            role = roleEnum,
            location = CityTable.get(location.value) ?: CityTable.initCities.first()
        )
    }

    companion object: IntEntityClass<UserEntity>(UserTable)
}