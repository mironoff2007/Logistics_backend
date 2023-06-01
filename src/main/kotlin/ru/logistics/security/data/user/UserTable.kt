package ru.logistics.security.data.user

import com.mironov.database.TablesConstants.USERS_TABLE_NAME
import com.mironov.database.TablesConstants.selectCountQuery
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object UserTable : IntIdTable(USERS_TABLE_NAME) {

    val userId = UserTable.long("user_id").uniqueIndex()
    val name = UserTable.varchar(name = "name", length = 50)
    val passwordHash = UserTable.varchar(name = "passwordHash", length = 50)
    val salt = UserTable.varchar(name = "salt", length = 50)

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
    fun replace(userEntity: UserEntity) {
        transaction {
            UserTable.replace {
                it[userId] = userEntity.sequelId
                it[name] = userEntity.name
                it[passwordHash] = userEntity.password
                it[salt] = userEntity.salt
            }
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
            }
        }
    }

    @Throws
    fun replaceAllTransaction(parcels: List<UserEntity>) {
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

}