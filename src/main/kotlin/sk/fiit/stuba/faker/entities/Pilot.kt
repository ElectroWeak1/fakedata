package sk.fiit.stuba.faker.entities

import org.jetbrains.exposed.dao.LongIdTable

object Pilot : LongIdTable("pilot") {
    val name = text("name")
    val hoursFlown = integer("hours_flown")
    val rank = reference("rank", PilotRank)
}