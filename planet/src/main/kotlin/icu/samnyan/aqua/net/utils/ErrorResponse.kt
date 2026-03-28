package icu.samnyan.aqua.net.utils

import ext.Str
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler


val SUCCESS = ResponseEntity.ok().body(mapOf("status" to "ok"))

class ApiException(val code: Int, message: Str) : RuntimeException(message) {
    companion object {
        val log = LoggerFactory.getLogger(ApiException::class.java)
    }

    fun resp() = ResponseEntity.status(code).body(message.toString())
}

fun Exception.simpleDescribe(): String = if (this is ApiException) "E${code}" else javaClass.simpleName

@ControllerAdvice(basePackages = ["icu.samnyan"])
class GlobalExceptionHandler {
    @ExceptionHandler(ApiException::class)
    fun handleCustomApiException(e: ApiException): ResponseEntity<String> {
        // On error, return the error code and message
        return e.resp()
    }
}