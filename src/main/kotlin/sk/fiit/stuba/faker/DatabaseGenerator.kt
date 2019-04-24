package sk.fiit.stuba.faker

import com.github.javafaker.Faker
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import sk.fiit.stuba.faker.entities.*
import sk.fiit.stuba.faker.models.*
import java.time.Instant
import java.util.*
import java.util.concurrent.TimeUnit

class DatabaseGenerator {
    val config = Properties().apply {
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

    fun insertRocketsToDb() = transaction {
        val rocketCount = config.getProperty(ROCKETS_KEY, "1000").toInt()
        rocketsSequence().take(rocketCount).forEach {rocket ->
            Rocket.insert {
                it[model] = rocket.model
                it[flown] = rocket.flown
                it[capacity] = rocket.capacity
                it[speed] = rocket.speed
                it[fuelConsumption] = rocket.fuelConsumption
                it[fuelCapacity] = rocket.fuelCapacity
            }
        }
    }

    fun insertHotelsToDb() = transaction {
        val hotelCount = config.getProperty(HOTELS_KEY, "1000").toInt()
        hotelsSequence().take(hotelCount).forEach { hotel ->
            Hotel.insert {
                it[name] = hotel.name
                it[price] = hotel.price
                it[address] = hotel.address
                it[phoneNum] = hotel.phoneNumber
                it[planet] = EntityID(hotel.planetId, Planet)
            }
        }
    }

    fun insertCustomersToDb() = transaction {
        val customerCount = config.getProperty(CUSTOMERS_KEY, "1000").toInt()
        customersSequence().take(customerCount).forEach { hotel ->
            Customer.insert {
                it[name] = hotel.name
                it[birthDate] = hotel.birthDate
                it[phoneNumber] = hotel.phoneNum
                it[email] = hotel.email
                it[street] = hotel.street
                it[postalCode] = hotel.postalCode
                it[city] = hotel.city
                it[country] = EntityID(hotel.countryId, Hotel)
            }
        }
    }

    fun insertTripsToDb() = transaction {
        val tripCount = config.getProperty(TRIPS_KEY, "10000").toInt()
        tripsSequence().take(tripCount).forEach { trip ->
            Trip.insert {
                it[startDate] = trip.startDate
                it[endDate] = trip.endDate
                it[capacity] = trip.capacity
                it[cost] = trip.cost
                it[pilot] = EntityID(trip.pilot, Pilot)
                it[rocket] = EntityID(trip.rocket, Rocket)
                it[hotel] = EntityID(trip.hotel, Hotel)
            }
        }
    }

    fun insertTripOrdersToDb() = transaction {
        val tripOrderCount = config.getProperty(TRIP_ORDERS_KEY, "1000").toInt()
        tripOrdersSequence().take(tripOrderCount).forEach { tripOrder ->
            TripOrder.insert {
                it[date] = tripOrder.date
                it[trip] = EntityID(tripOrder.tripId, Trip)
                it[customer] = EntityID(tripOrder.customerId, Customer)
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
                    flown = faker.random().nextInt(0, 999999999).toLong(),
                    capacity = faker.random().nextInt(1, 100000),
                    speed = faker.random().nextInt(100000).toDouble(),
                    fuelConsumption = faker.random().nextInt(1, 20000).toDouble(),
                    fuelCapacity = faker.random().nextInt(1, 200000)
            )
        }
    }

    private fun hotelsSequence(): Sequence<HotelModel> {
        return generateSequence {
            val address = faker.address()
            HotelModel(
                    name = faker.company().name() + " Hotel",
                    price = faker.random().nextInt(10, 1000000).toDouble(),
                    address = address.streetName() + ", " + address.cityName(),
                    phoneNumber = faker.random().nextInt(100000000, 999999999).toString(),
                    planetId = faker.random().nextInt(1, 8).toLong()
            )
        }
    }

    private fun customersSequence(): Sequence<CustomerModel> {
        var shouldGenerateFunnyName = true
        val address = faker.address()
        return generateSequence {
            shouldGenerateFunnyName = !shouldGenerateFunnyName
            CustomerModel(
                    name = if (shouldGenerateFunnyName) faker.funnyName().name() else faker.name().fullName(),
                    birthDate = DateTime(faker.date().birthday(18, 100).time),
                    phoneNum = faker.random().nextInt(100000000, 999999999).toString(),
                    email = faker.internet().emailAddress(),
                    street = address.streetAddress(),
                    postalCode = address.zipCode(),
                    city = address.cityName(),
                    countryId = faker.random().nextInt(1, 249).toLong()
            )
        }
    }

    private fun tripsSequence(): Sequence<TripModel> {
        val pilotsCount = config.getProperty(PILOTS_KEY).toInt()
        val rocketsCount = config.getProperty(ROCKETS_KEY).toInt()
        val hotelCount = config.getProperty(HOTELS_KEY).toInt()
        return generateSequence {
            val randomDate = faker.date().between(Date.from(Instant.parse("2010-01-01T00:00:00.00Z")), Date.from(Instant.parse("2020-01-01T00:00:00.00Z")))
            TripModel(
                    startDate = DateTime(faker.date().past(30, TimeUnit.DAYS, randomDate).time),
                    endDate = DateTime(faker.date().future(30, TimeUnit.DAYS, randomDate).time),
                    capacity = faker.random().nextInt(1, 10000),
                    cost = faker.random().nextInt(1, 100000).toDouble(),
                    pilot = faker.random().nextInt(1, pilotsCount - 1).toLong(),
                    rocket = faker.random().nextInt(1, rocketsCount - 1).toLong(),
                    hotel = faker.random().nextInt(1, hotelCount - 1).toLong()
            )
        }
    }

    private fun tripOrdersSequence(): Sequence<TripOrderModel> {
        val tripCount = config.getProperty(TRIPS_KEY).toInt()
        val customerCount = config.getProperty(CUSTOMERS_KEY).toInt()
        return generateSequence {
            val randomDate = DateTime(faker.date().between(Date.from(Instant.parse("2010-01-01T00:00:00.00Z")), Date.from(Instant.parse("2020-01-01T00:00:00.00Z"))).time)
            TripOrderModel(
                    date = randomDate,
                    tripId = faker.random().nextInt(1, tripCount - 1).toLong(),
                    customerId = faker.random().nextInt(1, customerCount - 1).toLong()
            )
        }
    }

    companion object {
        const val CONFIG_FILE_NAME = "config.properties"

        const val PILOTS_KEY = "PILOTS"
        const val ROCKETS_KEY = "ROCKETS"
        const val HOTELS_KEY = "HOTELS"
        const val CUSTOMERS_KEY = "CUSTOMERS"
        const val TRIPS_KEY = "TRIPS"
        const val TRIP_ORDERS_KEY = "TRIP_ORDERS"
    }
}