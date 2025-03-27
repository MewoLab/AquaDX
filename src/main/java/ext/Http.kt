package ext

import icu.samnyan.aqua.sega.util.ZLib
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration

val client = HttpClient.newBuilder().build()

fun HttpRequest.Builder.send() = client.send(this.build(), HttpResponse.BodyHandlers.ofByteArray())
fun HttpRequest.Builder.header(pair: Pair<Any, Any>) = this.header(pair.first.toString(), pair.second.toString())
fun String.request() = HttpRequest.newBuilder(URI.create(this)).timeout(Duration.ofMinutes(5))

fun HttpRequest.Builder.post(body: Any? = null) = this.POST(when (body) {
    is ByteArray -> HttpRequest.BodyPublishers.ofByteArray(body)
    is String -> HttpRequest.BodyPublishers.ofString(body)
    is HttpRequest.BodyPublisher -> body
    else -> throw IllegalArgumentException("Unsupported body type")
}).send()


inline fun <reified T> HttpResponse<String>.json(): T? = body()?.json()

fun HttpRequest.Builder.postZ(body: String) = run {
    header("Content-Type" to "application/json")
    header("Content-Encoding" to "deflate")
    post(ZLib.compress(body.toByteArray()))
}

fun <T> HttpResponse<T>.header(key: String) = headers().firstValue(key).orElse(null)
fun HttpResponse<ByteArray>.bodyString() = body()?.toString(Charsets.UTF_8)
fun HttpResponse<ByteArray>.bodyZ() = body()?.let { ZLib.decompress(it)?.decodeToString() }
fun HttpResponse<ByteArray>.bodyMaybeZ() =
    if (body().first().let { it != '{'.code.toByte() && it != '['.code.toByte() }) bodyZ()
    else bodyString()
