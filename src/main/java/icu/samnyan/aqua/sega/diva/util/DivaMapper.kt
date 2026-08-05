package icu.samnyan.aqua.sega.diva.util

import tools.jackson.core.json.JsonWriteFeature
import tools.jackson.core.type.TypeReference
import tools.jackson.databind.DeserializationFeature
import tools.jackson.databind.ObjectMapper
import tools.jackson.databind.ext.javatime.deser.LocalDateTimeDeserializer
import tools.jackson.databind.json.JsonMapper
import tools.jackson.databind.module.SimpleModule
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
    private val mapper: ObjectMapper = JsonMapper.builder()
        .addModule(SimpleModule().apply {
            addSerializer(LocalDateTime::class.java, DivaDateTimeSerializer())
            addDeserializer(LocalDateTime::class.java, LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.0")))
            addDeserializer(ZonedDateTime::class.java, ZonedDateTimeDeserializer())
            addSerializer(Boolean::class.java, BooleanNumberSerializer())
            addSerializer(Boolean::class.javaPrimitiveType, BooleanNumberSerializer())
            addDeserializer(Boolean::class.java, BooleanNumberDeserializer())
            addDeserializer(Boolean::class.javaPrimitiveType, BooleanNumberDeserializer())
        })
        .enable(JsonWriteFeature.WRITE_NUMBERS_AS_STRINGS)
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .build()

    fun write(o: Any) = mapper.writeValueAsString(o)
    fun <T> convert(map: JDict, toClass: Class<T>) = mapper.convertValue<T>(map, toClass)
    fun toMap(obj: Any) = mapper.convertValue(obj, object : TypeReference<LinkedHashMap<String, Any>>() {})
}
