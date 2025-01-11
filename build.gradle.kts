
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

plugins {
    val ktVer = "2.1.0"

    java
    kotlin("plugin.lombok") version ktVer
    kotlin("jvm") version ktVer
    kotlin("plugin.spring") version ktVer
    kotlin("plugin.jpa") version ktVer
    kotlin("plugin.serialization") version ktVer
    kotlin("plugin.allopen") version ktVer
    kotlin("kapt") version ktVer
    id("io.freefair.lombok") version "8.6"
    id("org.springframework.boot") version "3.2.3"
    id("com.github.ben-manes.versions") version "0.51.0"
    id("org.hibernate.orm") version "6.4.4.Final"
    application
}

apply(plugin = "io.spring.dependency-management")

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    // Spring boot
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-web") {
        exclude(group = "org.springframework.boot", module = "spring-boot-starter-tomcat")
    }
    implementation("org.springframework.boot:spring-boot-starter-jetty")
    implementation("io.netty:netty-all")
    implementation("org.apache.commons:commons-lang3:3.14.0")
    implementation("org.apache.httpcomponents.client5:httpclient5")
    implementation("org.flywaydb:flyway-core:10.10.0")
    implementation("org.flywaydb:flyway-mysql:10.10.0")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testImplementation("org.springframework.security:spring-security-test")
    implementation("net.logstash.logback:logstash-logback-encoder:7.4")

    // Metrics
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("io.micrometer:micrometer-registry-prometheus")

    // Database
    runtimeOnly("org.mariadb.jdbc:mariadb-java-client:3.3.3")
    runtimeOnly("org.xerial:sqlite-jdbc:3.45.2.0")
    implementation("org.hibernate.orm:hibernate-core:6.4.4.Final")
    implementation("org.hibernate.orm:hibernate-community-dialects:6.4.4.Final")
    implementation("io.github.openfeign.querydsl:querydsl-jpa:6.10.1")
    kapt("io.github.openfeign.querydsl:querydsl-apt:6.10.1:jpa")

    // JSR305 for nullable
    implementation("com.google.code.findbugs:jsr305:3.0.2")

    // =============================
    // AquaNet Specific Dependencies
    // =============================

    // Network
    implementation("io.ktor:ktor-client-core:2.3.8")
    implementation("io.ktor:ktor-client-cio:2.3.8")
    implementation("io.ktor:ktor-client-content-negotiation:2.3.8")
    implementation("io.ktor:ktor-client-encoding:2.3.8")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.8")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    // Somehow these are needed for ktor even though they're not in the documentation
    runtimeOnly("org.reactivestreams:reactive-streams:1.0.4")
    runtimeOnly("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.8.0")

    // Email
    implementation("org.simplejavamail:simple-java-mail:8.6.3")
    implementation("org.simplejavamail:spring-module:8.6.3")

    // GeoIP
    implementation("com.maxmind.geoip2:geoip2:4.2.0")

    // JWT Authentication
    implementation("io.jsonwebtoken:jjwt-api:0.12.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.5")

    // Content validation
    implementation("org.apache.tika:tika-core:2.9.1")

    // Import: DateTime Parsing
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.17.0")

    // Serialization
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // Testing
    testImplementation("io.kotest:kotest-runner-junit5-jvm:5.8.1")
    testImplementation("io.kotest:kotest-assertions-core")
}

group = "icu.samnya"
version = "1.0.0"
description = "AquaDX Arcade Server"
java.sourceCompatibility = JavaVersion.VERSION_21

kotlin {
    jvmToolchain(21)
}

springBoot {
    mainClass.set("icu.samnyan.aqua.EntryKt")
}

hibernate {
    enhancement {
        enableLazyInitialization = true
        enableAssociationManagement = false
        enableExtendedEnhancement = false
    }
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

val buildTime: String by extra(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z").withZone(ZoneId.of("UTC")).format(Instant.now()))

tasks.processResources {
    filesMatching("**/application.properties") {
        expand(project.properties)
    }
}

tasks.test {
    enabled = false
    useJUnitPlatform()
    jvmArgs("-Dkotest.assertions.collection.print.size=100")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<Javadoc> {
    options.encoding = "UTF-8"
}

tasks.getByName<Jar>("jar") {
    enabled = false
}

sourceSets {
    main {
        java.srcDir("${layout.buildDirectory.get()}/generated/source/kapt/main")
    }
}
