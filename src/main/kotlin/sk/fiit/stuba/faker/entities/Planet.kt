package sk.fiit.stuba.faker.entities

import org.jetbrains.exposed.dao.LongIdTable

object Planet : LongIdTable("planet") {
    val name = varchar("name", 50)
    val dayLength = double("day_length")
    val phonePrefix = varchar("phone_prefix", 5)
}