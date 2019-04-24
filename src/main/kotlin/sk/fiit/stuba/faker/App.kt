package sk.fiit.stuba.faker

import org.jetbrains.exposed.sql.Database
import java.util.*

class Config(private val fileName: String) {
    val config = Properties()

    init {
        config.load(this::class.java.classLoader.getResourceAsStream(fileName))
    }

    @Suppress("UNCHECKED_CAST")
    operator fun <T> get(key: String) = config[key] as T
}

fun main() {
    val config = Config("config.properties")

    Database.connect(
            config.get("DATABASE_URL"),
            driver = "org.postgresql.Driver",
            user = config["USER"],
            password = config["PASSWORD"]
    )

    val databaseGenerator = DatabaseGenerator()
    println("Generating pilots")
    databaseGenerator.insertPilotsToDb()
    println("Generating rockets")
    databaseGenerator.insertRocketsToDb()
    println("Generating hotels")
    databaseGenerator.insertHotelsToDb()
    println("Generating customers")
    databaseGenerator.insertCustomersToDb()
    println("Generating trips")
    databaseGenerator.insertTripsToDb()
    println("Generating trip orders")
    databaseGenerator.insertTripOrdersToDb()
}
