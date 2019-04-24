package sk.fiit.stuba.faker.entities

import org.jetbrains.exposed.dao.LongIdTable

object Rocket : LongIdTable("rocket") {
    val model = varchar("model", 100)
    val flown = long("km_flown")
    val capacity = integer("capacity")
    val speed = double("speed")
    val fuelConsumption = double("fuel_consumption")
    val fuelCapacity = integer("fuel_capacity")
}