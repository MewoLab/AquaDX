package icu.samnyan.aqua.sega.util.jackson

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.json.JsonWriteFeature
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import ext.jsonArray
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

open class IMapper(val mapper: ObjectMapper) {
    fun write(o: Any): String = mapper.writeValueAsString(o)
    fun <T> convert(map: Any, to: Class<T>) = mapper.convertValue(map, to)
    fun <T> convert(map: Any, to: TypeReference<T>): T = mapper.convertValue(map, to)
    fun <T> read(json: String, to: Class<T>) = mapper.readValue(json, to)
    fun <T> read(json: String, to: TypeReference<T>) = mapper.readValue(json, to)

    inline fun <reified T> convert(map: Any): T = convert(map, object : TypeReference<T>() {})
    inline fun <reified T> read(json: String): T = read(json, object : TypeReference<T>() {})
}

val BASIC_MAPPER = jacksonObjectMapper().apply {
    configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    configure(SerializationFeature.WRITE_ENUMS_USING_INDEX, true)
    findAndRegisterModules()
    registerModule(SimpleModule().apply {
        addSerializer(
            LocalDateTime::class.java,
            LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.0"))
        )
        addDeserializer(
            LocalDateTime::class.java,
            LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.0"))
        )
    })
}

@Component
class BasicMapper: IMapper(BASIC_MAPPER)



val BOOLEAN_SERIALIZER = object : StdSerializer<Boolean>(Boolean::class.java) {
    override fun serialize(v: Boolean, gen: JsonGenerator, p: SerializerProvider) {
        gen.writeString(v.toString())
    }
}

var STRING_MAPPER = jacksonObjectMapper().apply {
    enable(JsonWriteFeature.WRITE_NUMBERS_AS_STRINGS.mappedFeature())
    configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    configure(SerializationFeature.WRITE_ENUMS_USING_INDEX, true)
    findAndRegisterModules()
    registerModule(SimpleModule().apply {
        addSerializer(
            LocalDateTime::class.java,
            LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        )
        addDeserializer(
            LocalDateTime::class.java,
            LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        )
        addSerializer(Boolean::class.javaObjectType, BOOLEAN_SERIALIZER)
        addSerializer(Boolean::class.javaPrimitiveType, BOOLEAN_SERIALIZER)
    })
}

@Component
class StringMapper: IMapper(STRING_MAPPER)


// Testing code
private class A(
    var cat: String = ""
)

fun main(args: Array<String>) {
    val json = """{"cat":"meow"}"""
    val a = BasicMapper().read(json, A::class.java)
    println(a.cat)
    val lst = """[{"cat":"meow"}, {"cat":"meow"}]"""
    val b = BasicMapper().convert(lst.jsonArray(), object : TypeReference<List<A>>() {})
    println(b[0].cat)
    println(b.size)
    val c = BasicMapper().convert<List<A>>(lst.jsonArray())
    println(c[0].cat)
}
