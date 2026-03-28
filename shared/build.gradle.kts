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
}
