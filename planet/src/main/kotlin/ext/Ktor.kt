package ext

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*

val HTTP = HttpClient(CIO) {
    install(ContentNegotiation) {
        json(JSON)
    }
}
