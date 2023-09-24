package ru.logistics.database.user

import org.jetbrains.exposed.dao.id.EntityID
import ru.logistics.database.TablesConstants.USERS_TABLE_NAME
import ru.logistics.database.TablesConstants.selectCountQuery
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import ru.logistics.database.ParcelsTable
import ru.logistics.database.city.CityEntity
import ru.logistics.database.city.CityTable
import ru.logistics.security.data.user.User
import ru.mironov.logistics.UserRole

object UserTable : IntIdTable(USERS_TABLE_NAME) {

    val userId = UserTable.long("user_id").uniqueIndex()
    val name = UserTable.varchar(name = "name", length = 50)
    val passwordHash = UserTable.varchar(name = "passwordHash", length = 100)
    val salt = UserTable.varchar(name = "salt", length = 100)
    val role = UserTable.varchar(name = "role", length = 100).default(UserRole.COURIER.name)
    val location = UserTable
        .reference(
            name = "location",
            foreign = CityTable,
            onDelete = ReferenceOption.NO_ACTION,
            onUpdate = ReferenceOption.NO_ACTION
        )
        .default(CityEntity(EntityID(1, CityTable)).id)


    fun initDb(database: Database) {
        transaction(database) {
            SchemaUtils.createMissingTablesAndColumns(this@UserTable)
        }
    }

    @Throws
    fun clear() {
        transaction {
            UserTable.deleteAll()
        }
    }

    @Throws
    fun replace(user: User) {
        transaction {
            UserTable.replace {
                it[userId] = user.id
                it[name] = user.username
                it[passwordHash] = user.password
                it[salt] = user.salt
                it[role] = user.role.name
                it[role] = user.role.name
                it[location] = user.location.id
            }
        }
    }

    @Throws
    fun insert(user: User): ResultRow? {
        return transaction {
            return@transaction UserTable.insert {
                it[userId] = user.id
                it[name] = user.username
                it[passwordHash] = user.password
                it[salt] = user.salt
                it[role] = user.role.name
                it[location] = user.location.id
            }.resultedValues?.first()
        }
    }

    @Throws
    fun replaceAll(cities: List<User>) {
        transaction {
            UserTable.batchReplace(cities) {
                this[userId] = it.id
                this[name] = it.username
                this[passwordHash] = it.password
                this[salt] = it.salt
                this[role] = it.role.name
                this[location] = it.location.id
            }
        }
    }

    @Throws
    fun replaceAllTransaction(parcels: List<User>) {
        transaction {
            parcels.forEach { user ->
                replace(user)
            }
        }
    }

    @Throws
    fun count(): Int {
        var count = 0
        transaction {
            exec(selectCountQuery(USERS_TABLE_NAME)) { res ->
                res.next()
                count = res.getInt(1)
            }
        }
        return count
    }

    private fun fromRow(row: ResultRow): UserEntity {
        val i = row[id]
        return UserEntity[i]
    }

    /*private fun fromRow(row: ResultRow): User {
        val id = row[userId]
        val name = row[name]
        val password = row[passwordHash]
        val salt = row[salt]
        val currentCityId = row[location].value
        val role = try {
            UserRole.valueOf(row[role])
        }
        catch (e: Exception) {
            println(e)
            UserRole.COURIER
        }
        return User(
            id = id,
            username = name,
            password = password,
            salt = salt,
            role = role,
            location =  CityTable.get(currentCityId) ?: CityTable.initCities.first(),
        )
    }*/

    @Throws
    fun fetchAll(): List<User> {
        return try {
            transaction {
                UserTable.selectAll().toList()
                    .map {
                        fromRow(it).toUser()
                    }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    @Throws
    fun get(id: Long): User? {
        return try {
            transaction {
                UserTable.select {
                    userId eq id
                }.limit(1).single().let { fromRow(it).toUser() }
            }
        } catch (e: Exception) {
            null
        }
    }

    @Throws
    fun getByName(queryName: String): User? {
        return try {
            transaction {
                UserTable.select {
                    name eq queryName
                }.limit(1).single().let { fromRow(it).toUser() }
            }
        } catch (e: Exception) {
            null
        }
    }

}