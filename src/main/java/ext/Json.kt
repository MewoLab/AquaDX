package ext

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

// Jackson
val ACCEPTABLE_FALSE = setOf("0", "false", "no", "off", "False", "None", "null")
val ACCEPTABLE_TRUE = setOf("1", "true", "yes", "on", "True")
val JSON_FUZZY_BOOLEAN = SimpleModule().addDeserializer(Boolean::class.java, object : JsonDeserializer<Boolean>() {
    override fun deserialize(parser: JsonParser, context: DeserializationContext) = when(parser.text) {
        in ACCEPTABLE_FALSE -> false
        in ACCEPTABLE_TRUE -> true
        else -> 400 - "Invalid boolean value ${parser.text}"
    }
})
val JSON_DATETIME = SimpleModule().addDeserializer(java.time.LocalDateTime::class.java, object : JsonDeserializer<java.time.LocalDateTime>() {
    override fun deserialize(parser: JsonParser, context: DeserializationContext) =
        // First try standard formats via asDateTime() method
        parser.text.asDateTime() ?: try {
            // Try maimai2 format (yyyy-MM-dd HH:mm:ss.0)
            LocalDateTime.parse(parser.text, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.0"))
        } catch (e: Exception) {
            400 - "Invalid date time value ${parser.text}"
        }
})
val JACKSON = jacksonObjectMapper().apply {
    setSerializationInclusion(JsonInclude.Include.NON_NULL)
    setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL)
    findAndRegisterModules()
    registerModule(JSON_FUZZY_BOOLEAN)
    registerModule(JSON_DATETIME)
    configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    propertyNamingStrategy = PropertyNamingStrategies.LOWER_CAMEL_CASE;
}
inline fun <reified T> ObjectMapper.parse(str: Str) = readValue(str, T::class.java)
inline fun <reified T> ObjectMapper.parse(map: Map<*, *>) = convertValue(map, T::class.java)
// TODO: https://stackoverflow.com/q/78197784/7346633
fun <T> Str.parseJackson(cls: Class<T>) = if (contains("null")) {
    val map = JACKSON.parse<MutableMap<String, Any>>(this)
    JACKSON.convertValue(map.recursiveNotNull(), cls)
}
else JACKSON.readValue(this, cls)
fun <T> T.toJson() = JACKSON.writeValueAsString(this)

inline fun <reified T> String.json() = try {
    if (isEmpty() || this == "null") null
    else JACKSON.readValue(this, T::class.java)
}
catch (e: Exception) {
    println("Failed to parse JSON: $this")
    throw e
}

fun String.jsonMap(): Map<String, Any?> = json() ?: emptyMap()
fun String.jsonArray(): List<Map<String, Any?>> = json() ?: emptyList()
fun String.jsonMaybeMap(): Map<String, Any?>? = json()
fun String.jsonMaybeArray(): List<Map<String, Any?>>? = json()

// KotlinX Serialization
@OptIn(ExperimentalSerializationApi::class)
val JSON = Json {
    ignoreUnknownKeys = true
    isLenient = true
    namingStrategy = JsonNamingStrategy.SnakeCase
    explicitNulls = false
    coerceInputValues = true
}

// Bean for default jackson object mapper
//@Configuration
//class JacksonConfig {
//    @Bean
//    fun objectMapper(): ObjectMapper {
//        return JACKSON
//    }
//}
