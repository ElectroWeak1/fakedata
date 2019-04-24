package sk.fiit.stuba.faker

import org.jetbrains.exposed.sql.Database

fun main() {
    Database.connect(
            "jdbc:postgresql://localhost:5432/postgres",
            driver = "org.postgresql.Driver",
            user = "postgres",
            password = "postgres"
    )

    val databaseGenerator = DatabaseGenerator()
    databaseGenerator.insertPilotsToDb()
}
