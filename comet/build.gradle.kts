plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("application")
}

dependencies {
    implementation(project(":shared"))

    // Network
    implementation("io.ktor:ktor-client-core:3.0.3")
    implementation("io.ktor:ktor-client-cio:3.0.3")
    implementation("io.ktor:ktor-client-content-negotiation:3.0.3")
    implementation("io.ktor:ktor-serialization-kotlinx-json:3.0.3")

    // Serialization
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.17.0")
    
    // Testing
    testImplementation("io.kotest:kotest-runner-junit5-jvm:5.8.1")
}

application {
    mainClass = "icu.samnyan.aqua.comet.EntryKt"
}
