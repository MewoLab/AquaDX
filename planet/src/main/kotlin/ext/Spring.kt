package ext

import aquadx.net.utils.ApiException
import jakarta.persistence.Query
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.ApplicationContext
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity.BodyBuilder
import org.springframework.web.bind.annotation.*

typealias RP = RequestParam
typealias RB = RequestBody
typealias RT = RequestPart
typealias RH = RequestHeader
typealias PV = PathVariable
typealias API = RequestMapping

fun HttpServletRequest.details() = mapOf(
    "method" to method,
    "uri" to requestURI,
    "query" to queryString,
    "remote" to remoteAddr,
    "headers" to headerNames.asSequence().associateWith { getHeader(it) }
)

fun HttpServletResponse.details() = mapOf(
    "status" to status,
    "headers" to headerNames.asSequence().associateWith { getHeader(it) },
)

// HTTP
operator fun HttpStatus.invoke(message: String? = null): Nothing = throw ApiException(value(), message ?: this.reasonPhrase)

fun <R> parsing(block: () -> R) = try { block() }
catch (e: ApiException) { throw e }
catch (e: Exception) { 400 - e.message.toString() }
fun BodyBuilder.headers(vararg pairs: Pair<String, String>) = headers(HttpHeaders().apply { pairs.forEach { (k, v) -> set(k, v) } })

// Database
val Query.exec get() = resultList.map { (it as Array<*>).toList() }

// DI
inline fun <reified T> ApplicationContext.lazy() = lazy { getBean(T::class.java) }
