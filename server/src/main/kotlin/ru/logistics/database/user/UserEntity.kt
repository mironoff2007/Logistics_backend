package ru.logistics.database.user

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import ru.logistics.security.data.user.User

class UserEntity(id: EntityID<Int>): IntEntity(id) {
    var name by UserTable.name
    var sequelId by UserTable.userId
    var password by UserTable.passwordHash
    var salt by UserTable.salt

    fun toUser(): User {
        return User(
            id = sequelId,
            username = name,
            password = password,
            salt = salt
        )
    }

    companion object: IntEntityClass<UserEntity>(UserTable)
}