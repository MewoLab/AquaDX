package icu.samnyan.aqua.sega.cardmaker

import tools.jackson.core.type.TypeReference
import tools.jackson.databind.json.JsonMapper
import ext.logger
import jakarta.servlet.http.HttpServletRequest
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.nio.charset.StandardCharsets

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@RestControllerAdvice(basePackages = ["icu.samnyan.aqua.sega.cardmaker"])
class CardMakerControllerAdvice {
    val logger = logger()
    val mapper = JsonMapper()

    /**
     * Get the map object from json string
     *
     * @param request HttpServletRequest
     */
    @ModelAttribute
    fun preHandle(request: HttpServletRequest): MutableMap<String, Any> {
        val src = request.inputStream.readAllBytes()
        val outputString = String(src, StandardCharsets.UTF_8).trim { it <= ' ' }
        logger.info("Request ${request.requestURI}: $outputString")
        return mapper.readValue(outputString, object : TypeReference<MutableMap<String, Any>>() {})
    }
}