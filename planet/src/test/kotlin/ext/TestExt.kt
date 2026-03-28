package ext

import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlin.random.Random
import kotlin.random.nextInt

const val BOARD_ID = "ACAE-01A99999999"
const val FULL_CLIENT_ID = "A123-45678909999"
const val CLIENT_ID = "A1234567890"
const val FTK = "test"
const val HOST = "http://localhost"
val ACCESS_CODE = "9900" + (1..16).map { Random.nextInt(0..9) }.joinToString("")

suspend fun registerUser(): Long {
    val resp = HTTP.post(HOST.ensureEndingSlash() + "api/v2/frontier/register-card") {
        parameter("ftk", FTK)
        parameter("accessCode", ACCESS_CODE)
    }.bodyAsText()

    val userId = (resp.jsonMap()["id"] as Number).toLong()
    println("User ID: $userId")

    return userId
}