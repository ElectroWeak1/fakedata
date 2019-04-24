package sk.fiit.stuba.faker

import org.jetbrains.exposed.sql.Database

fun main() {
    val config = Config("config.properties")

    Database.connect(
            config["DATABASE_URL"],
            driver = "org.postgresql.Driver",
            user = config["USER"],
            password = config["PASSWORD"]
    )

    try {
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
    } catch (exception: ExceptionInInitializerError) {
        println("Couldn't insert rows into database. Please check your application configuration.")
    }
}
