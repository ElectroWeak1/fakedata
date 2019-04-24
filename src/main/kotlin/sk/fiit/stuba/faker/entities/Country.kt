package sk.fiit.stuba.faker.entities

import org.jetbrains.exposed.dao.LongIdTable

object Country : LongIdTable("country", "country_id") {
    val name = text("name")
    val phonePrefix = varchar("phone_prefix", 10)
}