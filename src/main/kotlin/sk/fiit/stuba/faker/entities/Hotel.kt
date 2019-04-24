package sk.fiit.stuba.faker.entities

import org.jetbrains.exposed.dao.LongIdTable

object Hotel : LongIdTable("hotel") {
    val name = varchar("name", 100)
    val price = double("price")
    val address = text("address")
    val phoneNum = varchar("phone_num", 20)
    val planet = reference("planet", Planet)
}