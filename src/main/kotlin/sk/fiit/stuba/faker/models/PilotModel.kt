package sk.fiit.stuba.faker.models

import org.jetbrains.exposed.dao.EntityID
import sk.fiit.stuba.faker.entities.PilotRank

data class PilotModel(
        val id: Long = 0,
        val name: String = "",
        val hoursFlown: Int = 0
) {
    val rank: EntityID<Long> by lazy(LazyThreadSafetyMode.NONE) {
        EntityID(when {
            hoursFlown < 10 -> 1L
            hoursFlown < 25 -> 2L
            hoursFlown < 50 -> 3L
            hoursFlown < 100 -> 4L
            else -> 5L
        }, PilotRank)
    }
}