package sk.fiit.stuba.faker.models

data class HotelModel(
        val name: String = "",
        val price: Double = 0.0,
        val address: String = "",
        val phoneNumber: String = "",
        val planetId: Long = 0L
)