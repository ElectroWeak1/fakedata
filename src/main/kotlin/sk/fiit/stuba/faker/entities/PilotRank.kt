package sk.fiit.stuba.faker.entities

import org.jetbrains.exposed.dao.LongIdTable

object PilotRank : LongIdTable("pilot_rank") {
    val name = varchar("name", 50)
    val timeFlown = long("time_flown")
}