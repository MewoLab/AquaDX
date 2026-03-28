package icu.samnyan.aqua.net.utils

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler


val SUCCESS: ResponseEntity<Map<String, String>> = ResponseEntity.ok().body(mapOf("status" to "ok"))

fun ApiException.resp(): ResponseEntity<String> = ResponseEntity.status(code).body(message.toString())

fun Exception.simpleDescribe(): String = if (this is ApiException) "E${code}" else javaClass.simpleName

@ControllerAdvice(basePackages = ["icu.samnyan"])
class GlobalExceptionHandler {
    @ExceptionHandler(ApiException::class)
    fun handleCustomApiException(e: ApiException): ResponseEntity<String> {
        // On error, return the error code and message
        return e.resp()
    }
}