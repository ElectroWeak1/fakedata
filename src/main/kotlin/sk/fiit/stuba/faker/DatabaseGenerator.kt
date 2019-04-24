package sk.fiit.stuba.faker

import com.github.javafaker.Faker
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.batchInsert
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
    val batchChunkSize = config.getProperty("BATCH_CHUNK_SIZE").toInt()
    val faker = Faker()

    fun insertPilotsToDb() {
        val pilotsCount = config.getProperty(PILOTS_KEY, "1000").toInt()
        var i = 1
        val batches = pilotsCount / batchChunkSize
        pilotsSequence().take(pilotsCount).chunked(batchChunkSize).forEach {
            transaction {
                println("Inserting pilots, batch ${i++}/$batches")
                Pilot.batchInsert(it.toList()) { pilot ->
                    this[Pilot.name] = pilot.name
                    this[Pilot.hoursFlown] = pilot.hoursFlown
                    this[Pilot.rank] = pilot.rank
                }
            }
        }
    }

    fun insertRocketsToDb() {
        val rocketCount = config.getProperty(ROCKETS_KEY, "1000").toInt()
        var i = 1
        val batches = rocketCount / batchChunkSize
        rocketsSequence().take(rocketCount).chunked(batchChunkSize).forEach {
            transaction {
                println("Inserting rockets, batch ${i++}/$batches")
                Rocket.batchInsert(it.toList()) { rocket ->
                    this[Rocket.model] = rocket.model
                    this[Rocket.flown] = rocket.flown
                    this[Rocket.capacity] = rocket.capacity
                    this[Rocket.speed] = rocket.speed
                    this[Rocket.fuelConsumption] = rocket.fuelConsumption
                    this[Rocket.fuelCapacity] = rocket.fuelCapacity
                }
            }
        }
    }

    fun insertHotelsToDb() {
        val hotelCount = config.getProperty(HOTELS_KEY, "1000").toInt()
        var i = 1
        val batches = hotelCount / batchChunkSize
        hotelsSequence().take(hotelCount).chunked(batchChunkSize).forEach {
            transaction {
                println("Inserting hotels, batch ${i++}/$batches")
                Hotel.batchInsert(it.toList()) { hotel ->
                    this[Hotel.name] = hotel.name
                    this[Hotel.price] = hotel.price
                    this[Hotel.address] = hotel.address
                    this[Hotel.phoneNum] = hotel.phoneNumber
                    this[Hotel.planet] = EntityID(hotel.planetId, Planet)
                }
            }
        }
    }

    fun insertCustomersToDb() {
        val customerCount = config.getProperty(CUSTOMERS_KEY, "1000").toInt()
        var i = 1
        val batches = customerCount / batchChunkSize
        customersSequence().take(customerCount).chunked(batchChunkSize).forEach {
            transaction {
                println("Inserting customers, batch ${i++}/$batches")
                Customer.batchInsert(it.toList()) { customer ->
                    this[Customer.name] = customer.name
                    this[Customer.birthDate] = customer.birthDate
                    this[Customer.phoneNumber] = customer.phoneNum
                    this[Customer.email] = customer.email
                    this[Customer.street] = customer.street
                    this[Customer.postalCode] = customer.postalCode
                    this[Customer.city] = customer.city
                    this[Customer.country] = EntityID(customer.countryId, Hotel)
                }
            }
        }
    }

    fun insertTripsToDb() {
        val tripCount = config.getProperty(TRIPS_KEY, "10000").toInt()
        var i = 1
        val batches = tripCount / batchChunkSize
        tripsSequence().take(tripCount).chunked(batchChunkSize).forEach {
            transaction {
                println("Inserting trips, batch ${i++}/$batches")
                Trip.batchInsert(it.toList()) { trip ->
                    this[Trip.startDate] = trip.startDate
                    this[Trip.endDate] = trip.endDate
                    this[Trip.capacity] = trip.capacity
                    this[Trip.cost] = trip.cost
                    this[Trip.pilot] = EntityID(trip.pilot, Pilot)
                    this[Trip.rocket] = EntityID(trip.rocket, Rocket)
                    this[Trip.hotel] = EntityID(trip.hotel, Hotel)
                }
            }
        }
    }

    fun insertTripOrdersToDb() {
        val tripOrderCount = config.getProperty(TRIP_ORDERS_KEY, "1000").toInt()
        var i = 1
        val batches = tripOrderCount / batchChunkSize
        tripOrdersSequence().take(tripOrderCount).chunked(batchChunkSize).forEach {
            transaction {
                println("Inserting trip orders, batch ${i++}/$batches")
                TripOrder.batchInsert(it.toList()) { tripOrder ->
                    this[TripOrder.date] = tripOrder.date
                    this[TripOrder.trip] = EntityID(tripOrder.tripId, Trip)
                    this[TripOrder.customer] = EntityID(tripOrder.customerId, Customer)
                }
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