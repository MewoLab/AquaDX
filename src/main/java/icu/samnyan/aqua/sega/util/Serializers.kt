package icu.samnyan.aqua.sega.util

import tools.jackson.core.JsonGenerator
import tools.jackson.core.JsonParser
import tools.jackson.core.JsonToken
import tools.jackson.databind.DeserializationContext
import tools.jackson.databind.SerializationContext
import tools.jackson.databind.ValueDeserializer
import tools.jackson.databind.ser.std.StdSerializer
import ext.int
import icu.samnyan.aqua.sega.general.model.Card
import java.time.ZonedDateTime

class AccessCodeSerializer @JvmOverloads constructor(t: Class<Card>? = null) : StdSerializer<Card>(t) {
    override fun serialize(value: Card, gen: JsonGenerator, provider: SerializationContext) {
        gen.writeString(value.luid)
    }
}

class BooleanToIntegerDeserializer : ValueDeserializer<Int>() {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext) = when (p.currentToken()) {
        JsonToken.VALUE_STRING -> when (val str = p.valueAsString.lowercase()) {
            "true" -> 1
            "false" -> 0
            else -> str.int
        }
        JsonToken.VALUE_NUMBER_INT -> p.intValue
        JsonToken.VALUE_TRUE -> 1
        JsonToken.VALUE_FALSE -> 0
        else -> throw UnsupportedOperationException("Cannot deserialize to boolean int")
    }
}

class BooleanNumberSerializer @JvmOverloads constructor(t: Class<Boolean>? = null) : StdSerializer<Boolean>(t) {
    override fun serialize(value: Boolean, gen: JsonGenerator, provider: SerializationContext) {
        gen.writeNumber(if (value) 1 else 0)
    }
}

class BooleanNumberDeserializer : ValueDeserializer<Boolean>() {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext) = "0" != p.text
}

class ZonedDateTimeDeserializer : ValueDeserializer<ZonedDateTime>() {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext) = ZonedDateTime.parse(p.text)
}

