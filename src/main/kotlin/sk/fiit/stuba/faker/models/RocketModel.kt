package sk.fiit.stuba.faker.models

data class RocketModel(
        val id: Long = 0L,
        val model: String = "",
        val flown: Int = 0,
        val capacity: Int = 0,
        val speed: Int = 0,
        val fuelConsumption: Int = 0,
        val fuelCapacity: Int = 0
)