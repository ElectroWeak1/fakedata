plugins {
    kotlin("jvm") version "1.3.30"
    application
}

repositories {
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.2.0")
    implementation("org.jetbrains.exposed:exposed:0.13.6")
    implementation("com.github.javafaker:javafaker:0.17.2")
    implementation("org.postgresql:postgresql:42.2.5")
    implementation("org.slf4j:slf4j-api:1.7.26")
    implementation("org.slf4j:slf4j-simple:1.7.26")
    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit"))
}

application {
    mainClassName = "sk.fiit.stuba.faker.AppKt"
    applicationDefaultJvmArgs = listOf("-Dkotlinx.coroutines.debug")
}
