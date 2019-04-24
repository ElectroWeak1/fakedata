package sk.fiit.stuba.faker

import com.github.javafaker.Faker
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import sk.fiit.stuba.faker.models.PilotModel
import sk.fiit.stuba.faker.entities.Pilot
import sk.fiit.stuba.faker.models.HotelModel
import sk.fiit.stuba.faker.models.RocketModel
import java.util.*

class DatabaseGenerator {
    val config = Properties(NO_OF_TABLES).apply {
        load(DatabaseGenerator::class.java.classLoader.getResourceAsStream(CONFIG_FILE_NAME))
    }
    val faker = Faker()

    fun insertPilotsToDb() = transaction {
        val pilotsCount = config.getProperty(PILOTS_KEY, "1000").toInt()
        val pilots = pilotsSequence()

        pilots.take(pilotsCount).forEach { pilot ->
            Pilot.insert {
                it[name] = pilot.name
                it[hoursFlown] = pilot.hoursFlown
                it[rank] = pilot.rank
            }
        }
    }

    private fun pilotsSequence(): Sequence<PilotModel> {
        var shouldGenerateFunnyName = true
        return generateSequence {
            shouldGenerateFunnyName = !shouldGenerateFunnyName
            PilotModel(
                    name = if (shouldGenerateFunnyName) faker.funnyName().name() else faker.name().fullName(),
                    hoursFlown = faker.random().nextInt(500)
            )
        }
    }

    private fun rocketsSequence(): Sequence<RocketModel> {
        return generateSequence {
            RocketModel(
                    model = faker.company().name(),
                    flown = faker.random().nextInt(0, 999999999),
                    capacity = faker.random().nextInt(1, 100000),
                    speed = faker.random().nextInt(100000),
                    fuelConsumption = faker.random().nextInt(1, 20000),
                    fuelCapacity = faker.random().nextInt(1, 200000)
            )
        }
    }

    private fun hotelsSequence(): Sequence<HotelModel> {
        return generateSequence {
            val address = faker.address()
            HotelModel(
                    name = faker.company().name() + " Hotel",
                    price = faker.random().nextInt(10, 1000000),
                    address = address.streetName() + ", " + address.city(),
                    phoneNumber = faker.random().nextInt(100000000, 999999999),
                    planetId = faker.random().nextInt(1, 8)
            )
        }
    }

    companion object {
        const val CONFIG_FILE_NAME = "config.properties"

        const val PILOTS_KEY = "PILOTS"
        const val ROCKETS_KEY = "ROCKETS"
        const val HOTELS_KEY = "HOTELS"
        const val TRIPS_KEY = "TRIPS"
        const val TRIP_ORDERS_KEY = "TRIP_ORDERS"

        const val NO_OF_TABLES = 5
    }
}