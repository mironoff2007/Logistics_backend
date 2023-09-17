package ru.logistics.security.data.user

import ru.logistics.TablesConstants.USERS_TABLE_NAME
import ru.logistics.TablesConstants.selectCountQuery
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object UserTable : IntIdTable(USERS_TABLE_NAME) {

    val userId = UserTable.long("user_id").uniqueIndex()
    val name = UserTable.varchar(name = "name", length = 50)
    val passwordHash = UserTable.varchar(name = "passwordHash", length = 100)
    val salt = UserTable.varchar(name = "salt", length = 100)

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

    private fun fromRow(row: ResultRow): User {
        val id = row[userId]
        val name = row[name]
        val password = row[passwordHash]
        val salt = row[salt]
        return User(
            id = id,
            username = name,
            password = password,
            salt = salt
        )
    }

    @Throws
    fun fetchAll(): List<User> {
        return try {
            transaction {
                UserTable.selectAll().toList()
                    .map {
                        fromRow(it)
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
                }.limit(1).single().let { fromRow(it) }
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
                }.limit(1).single().let { fromRow(it) }
            }
        } catch (e: Exception) {
            null
        }
    }

}