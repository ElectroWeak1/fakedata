package sk.fiit.stuba.faker.models

import org.joda.time.DateTime

data class TripOrderModel(
        val id: Long = 0L,
        val date: DateTime = DateTime.now(),
        val tripId: Long = 0,
        val customerId: Long = 0
)