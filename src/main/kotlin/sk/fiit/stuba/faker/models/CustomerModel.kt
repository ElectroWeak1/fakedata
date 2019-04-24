package sk.fiit.stuba.faker.models

import org.joda.time.DateTime

data class CustomerModel(
        val id: Long = 0L,
        val name: String = "",
        val birthDate: DateTime = DateTime.now(),
        val phoneNum: String = "",
        val email: String = "",
        val street: String = "",
        val postalCode: String = "",
        val city: String = "",
        val countryId: Long = 0L
)