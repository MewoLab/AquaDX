package icu.samnyan.aqua.sega.diva.handler

import ext.JDict
import icu.samnyan.aqua.sega.diva.util.DivaMapper
import org.springframework.stereotype.Component

fun buildResultMap(map: JDict) =
    map.filterValues { it != null && !(it is String && it == "") }
        .map { (k, v) -> "$k=$v" }.joinToString("&")

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
class BaseHandler {
    @JvmField
    final var mapper = DivaMapper()

    fun build(map: JDict) = buildResultMap(map)
}
