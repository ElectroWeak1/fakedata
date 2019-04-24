package sk.fiit.stuba.faker.entities

import org.jetbrains.exposed.dao.LongIdTable

object TripOrder : LongIdTable("trip_order") {
    val date = date("date")
    val trip = reference("trip_id", Trip)
    val customer = reference("customer_id", Customer)
}