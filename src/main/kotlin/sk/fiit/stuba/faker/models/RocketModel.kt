package sk.fiit.stuba.faker.models

data class RocketModel(
        val id: Long = 0L,
        val model: String = "",
        val flown: Long = 0,
        val capacity: Int = 0,
        val speed: Double = 0.0,
        val fuelConsumption: Double = 0.0,
        val fuelCapacity: Int = 0
)