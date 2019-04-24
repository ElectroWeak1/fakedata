package sk.fiit.stuba.faker.entities

import org.jetbrains.exposed.dao.LongIdTable

object Customer : LongIdTable("customer") {
    val name = text("name")
    val birthDate = date("birth_date")
    val phoneNumber = varchar("phone_num", 10)
    val email = varchar("email", 255)
    val street = text("street")
    val postalCode = varchar("postal_code", 5)
    val city = text("city")
    val country = reference("country_id", Country)
}