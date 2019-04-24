package sk.fiit.stuba.faker.entities

import org.jetbrains.exposed.dao.LongIdTable

object Trip : LongIdTable("trip") {
    val startDate = date("start_date")
    val endDate = date("end_date")
    val capacity = integer("capacity")
    val cost = double("cost")
    val pilot = reference("pilot", Pilot)
    val rocket = reference("rocket", Rocket)
    val hotel = reference("hotel", Hotel)
}