package icu.samnyan.aqua.sega.general

import com.fasterxml.jackson.core.JsonProcessingException

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
fun interface BaseHandler {
    @Throws(JsonProcessingException::class)
    fun handle(request: Map<String, Any>): Any?
}
