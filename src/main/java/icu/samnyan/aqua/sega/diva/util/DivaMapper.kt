package icu.samnyan.aqua.sega.diva.util

import com.fasterxml.jackson.core.json.JsonWriteFeature
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import ext.JDict
import icu.samnyan.aqua.sega.util.BooleanNumberDeserializer
import icu.samnyan.aqua.sega.util.BooleanNumberSerializer
import icu.samnyan.aqua.sega.util.ZonedDateTimeDeserializer
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Component
class DivaMapper {
    private val mapper: ObjectMapper = JsonMapper.builder().enable(JsonWriteFeature.WRITE_NUMBERS_AS_STRINGS)
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .build().apply {
            registerModule(SimpleModule().apply {
                addSerializer(LocalDateTime::class.java, DivaDateTimeSerializer())
                addDeserializer(LocalDateTime::class.java, LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.0")))
                addDeserializer(ZonedDateTime::class.java, ZonedDateTimeDeserializer())
                addSerializer(Boolean::class.java, BooleanNumberSerializer())
                addSerializer(Boolean::class.javaPrimitiveType, BooleanNumberSerializer())
                addDeserializer(Boolean::class.java, BooleanNumberDeserializer())
                addDeserializer(Boolean::class.javaPrimitiveType, BooleanNumberDeserializer())
            })
        }

    fun write(o: Any) = mapper.writeValueAsString(o)
    fun <T> convert(map: JDict, toClass: Class<T>) = mapper.convertValue<T>(map, toClass)
    fun toMap(obj: Any) = mapper.convertValue(obj, object : TypeReference<LinkedHashMap<String, Any>>() {})
}
