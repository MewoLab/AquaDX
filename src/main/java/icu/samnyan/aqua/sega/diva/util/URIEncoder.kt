package icu.samnyan.aqua.sega.diva.util

import java.net.URLEncoder
import java.nio.charset.StandardCharsets

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
object URIEncoder {
    @JvmStatic
    fun encode(str: String) = URLEncoder.encode(str, StandardCharsets.UTF_8).replace("\\+".toRegex(), "%20")
}