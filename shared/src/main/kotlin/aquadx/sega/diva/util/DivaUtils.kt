package aquadx.sega.diva.util

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object DivaStringUtils {
    @JvmStatic
    fun getDummyString(content: String, length: Int) = ",".repeat(length).removeSuffix(",")
}

object DivaTime {
    val now get() = getString(LocalDateTime.now())

    @JvmStatic
    fun getString(time: LocalDateTime) = URIEncoder.encode(format(time))

    @JvmStatic
    fun format(time: LocalDateTime) = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.0").format(time)
}

class DivaDateTimeSerializer(t: Class<LocalDateTime>? = null) : StdSerializer<LocalDateTime>(t) {
    override fun serialize(value: LocalDateTime, gen: JsonGenerator, provider: SerializerProvider) {
        gen.writeString(DivaTime.getString(value))
    }
}

object URIEncoder {
    @JvmStatic
    fun encode(str: String) = URLEncoder.encode(str, StandardCharsets.UTF_8).replace("\\+".toRegex(), "%20")
}
