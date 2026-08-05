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
// ---------------------------------------------------------------------------
// Live-server test gating
// ---------------------------------------------------------------------------

/**
 * Integration tests (Mai2Test, WaccaTest) talk to a real AquaDX server on
 * localhost:80 backed by MariaDB. When no server is listening, tests
 * registered via [liveTest] are marked as disabled instead of failing.
 */
val LIVE_SERVER_AVAILABLE: Boolean by lazy {
    runCatching {
        java.net.Socket().use { socket ->
            socket.connect(java.net.InetSocketAddress("localhost", 80), 1000)
            true
        }
    }.onFailure { println("No live server on localhost:80 - live-server tests will be skipped") }
        .getOrDefault(false)
}

/** Registers a test that only runs when [LIVE_SERVER_AVAILABLE] is true. */
fun io.kotest.core.spec.style.scopes.StringSpecRootScope.liveTest(
    name: String,
    test: suspend io.kotest.core.test.TestScope.() -> Unit,
) = name.config(enabled = LIVE_SERVER_AVAILABLE, test = test)
