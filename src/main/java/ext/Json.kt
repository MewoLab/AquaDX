package ext

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
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
val JSON_DATETIME = SimpleModule()
    .addSerializer(LocalDateTime::class.java, LocalDateTimeSerializer(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
    .addDeserializer(LocalDateTime::class.java, object : JsonDeserializer<LocalDateTime>() {
        override fun deserialize(parser: JsonParser, context: DeserializationContext): LocalDateTime? {
            parser.readLocalDateTimeArray(context)?.let { return it }

            // First try standard formats via asDateTime() method
            return parser.text.takeIf { it.isNotEmpty() }?.run { asDateTime() ?: try {
                // Try maimai2 format (yyyy-MM-dd HH:mm:ss.0)
                LocalDateTime.parse(parser.text, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.0"))
            } catch (e: Exception) {
                400 - "Invalid date time value ${parser.text}"
            } }
        }
    })

fun JsonParser.readLocalDateTimeArray(context: DeserializationContext): LocalDateTime? {
    if (!isExpectedStartArrayToken) return null

    val parts = mutableListOf<Int>()
    while (nextToken() != JsonToken.END_ARRAY) {
        if (currentToken() != JsonToken.VALUE_NUMBER_INT) {
            throw context.weirdStringException(
                parts.toString(),
                LocalDateTime::class.java,
                "Invalid legacy date-time array",
            )
        }
        parts += intValue
    }

    if (parts.size !in 5..7) {
        throw context.weirdStringException(
            parts.toString(),
            LocalDateTime::class.java,
            "Invalid legacy date-time array; expected 5 to 7 integer components",
        )
    }

    return try {
        LocalDateTime.of(
            parts[0], parts[1], parts[2], parts[3], parts[4],
            parts.getOrElse(5) { 0 }, parts.getOrElse(6) { 0 },
        )
    } catch (e: Exception) {
        throw context.weirdStringException(
            parts.toString(),
            LocalDateTime::class.java,
            "Invalid legacy date-time array: ${e.message}",
        )
    }
}
val JACKSON = jacksonObjectMapper().apply {
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
