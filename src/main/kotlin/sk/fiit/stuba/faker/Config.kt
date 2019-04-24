package sk.fiit.stuba.faker

import java.util.*

class Config(fileName: String) {
    private val config = Properties()

    init {
        config.load(this::class.java.classLoader.getResourceAsStream(fileName))
    }

    @Suppress("UNCHECKED_CAST")
    operator fun <T> get(key: String) = config[key] as T
}