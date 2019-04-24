package sk.fiit.stuba.faker.models

import org.joda.time.DateTime

data class TripModel(
        val id: Long = 0,
        val startDate: DateTime = DateTime.now(),
        val endDate: DateTime = DateTime.now(),
        val capacity: Int = 0,
        val cost: Double = 0.0,
        val pilot: Long = 0,
        val rocket: Long = 0,
        val hotel: Long = 0
)