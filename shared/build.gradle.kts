plugins {
    kotlin("jvm")
    kotlin("plugin.jpa")
    kotlin("plugin.serialization")
}

dependencies {
    api("jakarta.persistence:jakarta.persistence-api:3.1.0")
    api("jakarta.servlet:jakarta.servlet-api:6.0.0")
    api("com.fasterxml.jackson.core:jackson-annotations:2.17.0")
    api("com.fasterxml.jackson.core:jackson-databind:2.17.0")
    api("com.fasterxml.jackson.module:jackson-module-kotlin:2.17.0")

    // Core libraries
    api("org.slf4j:slf4j-api:2.0.12")
    api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
    api("org.jetbrains.kotlin:kotlin-reflect:2.1.10")
}
