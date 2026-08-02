
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    val ktVer = "2.4.0"

    java
    kotlin("jvm") version ktVer
    kotlin("plugin.spring") version ktVer
    kotlin("plugin.jpa") version ktVer
    kotlin("plugin.serialization") version ktVer
    kotlin("plugin.allopen") version ktVer
    kotlin("kapt") version ktVer
    id("org.springframework.boot") version "4.1.0"
    id("com.github.ben-manes.versions") version "0.54.0"
    id("org.hibernate.orm") version "7.4.4.Final"
    application
}

apply(plugin = "io.spring.dependency-management")

repositories {
    mavenLocal()
    mavenCentral()
}

extra["netty.version"] = "4.2.16.Final"
extra["kotlin-coroutines.version"] = "1.11.0"

dependencies {
    // Spring boot
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-web") {
        exclude(group = "org.springframework.boot", module = "spring-boot-starter-tomcat")
    }
    implementation("org.springframework.boot:spring-boot-starter-jetty")
    implementation(enforcedPlatform("io.netty:netty-bom:4.2.16.Final"))
    implementation("io.netty:netty-all:4.2.16.Final")
    implementation("org.apache.commons:commons-lang3:3.20.0")
    implementation("org.apache.httpcomponents.client5:httpclient5:5.6.2")
    implementation("org.springframework.boot:spring-boot-starter-flyway")
    implementation("org.flywaydb:flyway-core:12.10.0")
    implementation("org.flywaydb:flyway-mysql:12.10.0")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testImplementation("org.springframework.security:spring-security-test")
    implementation("net.logstash.logback:logstash-logback-encoder:9.0")

    // Metrics
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("io.micrometer:micrometer-registry-prometheus")

    // Database
    runtimeOnly("org.mariadb.jdbc:mariadb-java-client:3.5.9")
    runtimeOnly("org.xerial:sqlite-jdbc:3.53.2.0")
    implementation("org.hibernate.orm:hibernate-core:7.4.4.Final")
    implementation("org.hibernate.orm:hibernate-community-dialects:7.4.4.Final")
    implementation("io.github.openfeign.querydsl:querydsl-jpa:7.4.0")
    kapt("io.github.openfeign.querydsl:querydsl-apt:7.4.0:jpa")

    // JSR305 for nullable
    implementation("com.google.code.findbugs:jsr305:3.0.2")

    // =============================
    // AquaNet Specific Dependencies
    // =============================

    // Network
    implementation("io.ktor:ktor-client-core:3.5.1")
    implementation("io.ktor:ktor-client-cio:3.5.1")
    implementation("io.ktor:ktor-client-content-negotiation:3.5.1")
    implementation("io.ktor:ktor-client-encoding:3.5.1")
    implementation("io.ktor:ktor-serialization-kotlinx-json:3.5.1")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    // Somehow these are needed for ktor even though they're not in the documentation
    runtimeOnly("org.reactivestreams:reactive-streams:1.0.4")
    runtimeOnly("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.11.0")

    // Email
    implementation("org.simplejavamail:simple-java-mail:9.0.1")
    implementation("org.simplejavamail:spring-module:9.0.1")

    // GeoIP
    implementation("com.maxmind.geoip2:geoip2:5.1.0")

    // JWT Authentication
    implementation("io.jsonwebtoken:jjwt-api:0.13.0")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.13.0")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.13.0")

    // Content validation
    implementation("org.apache.tika:tika-core:3.3.1")

    // Import: DateTime Parsing
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.22.1")

    // Serialization
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.22.1")

    // Testing
    testImplementation("io.kotest:kotest-runner-junit5-jvm:6.2.2")
    testImplementation("io.kotest:kotest-assertions-core:6.2.2")
}

group = "icu.samnya"
version = "1.0.0"
description = "AquaDX Arcade Server"

java {
    sourceCompatibility = JavaVersion.VERSION_25
    targetCompatibility = JavaVersion.VERSION_25
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

kotlin {
    jvmToolchain(25)
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_25)
    }
}

springBoot {
    mainClass.set("icu.samnyan.aqua.EntryKt")
}

application {
    mainClass = "icu.samnyan.aqua.EntryKt"
}

kapt {
    includeCompileClasspath = false
    keepJavacAnnotationProcessors = true
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

val buildTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z").withZone(ZoneId.of("UTC")).format(Instant.now())
val projectVersion = version.toString()
extra["buildTime"] = buildTime

tasks.processResources {
    filesMatching("**/application.properties") {
        expand(
            mapOf(
                "version" to projectVersion,
                "ext" to mapOf("buildTime" to buildTime),
            )
        )
    }
}

tasks.test {
    enabled = providers.gradleProperty("runTests").isPresent
    useJUnitPlatform()
    jvmArgs("-Dkotest.assertions.collection.print.size=100")
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

tasks.withType<Javadoc>().configureEach {
    options.encoding = "UTF-8"
}

tasks.named<Jar>("jar") {
    enabled = false
}

sourceSets {
    main {
        java.srcDir("${layout.buildDirectory.get()}/generated/source/kapt/main")
    }
}

val copyDependencies = tasks.register<Copy>("copyDependencies") {
    from(configurations.runtimeClasspath)
    into("${layout.buildDirectory.get()}/libs/lib")
}

tasks.register<Jar>("packageThin") {
    group = "build"
    from(sourceSets.main.get().output)
    manifest {
        attributes(
            "Main-Class" to "icu.samnyan.aqua.EntryKt",
            "Class-Path" to configurations.runtimeClasspath.get().files.joinToString(" ") { "lib/${it.name}" }
        )
    }
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    dependsOn(copyDependencies)
}
