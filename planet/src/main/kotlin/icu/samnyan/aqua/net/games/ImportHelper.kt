package icu.samnyan.aqua.net.games

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import ext.JACKSON
import ext.minus

// Read SQL dump and convert to dictionary
val insertPattern = """INSERT INTO\s+(\w+)\s*\(([^)]+)\)\s*VALUES\s*\(([^)]+)\);""".toRegex()
data class SqlInsert(val table: String, val mapping: Map<String, String>)
fun String.asSqlInsert(): SqlInsert {
    val match = insertPattern.matchEntire(this) ?: error("Does not match insert pattern")
    val (table, rawCols, rawVals) = match.destructured
    val cols = rawCols.split(',').map { it.trim(' ', '"') }

    // Parse values with proper quote handling
    val vals = mutableListOf<String>()
    var startI = 0
    var insideQuote = false
    rawVals.forEachIndexed { i, c ->
        if (c == ',' && !insideQuote) {
            vals.add(rawVals.substring(startI, i).trim(' ', '"'))
            startI = i + 1
        } else if (c == '"') insideQuote = !insideQuote
    }

    assert(cols.size == vals.size) { "Column and value count mismatch" }
    return SqlInsert(table, cols.zip(vals).toMap())
}

@Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN", "UNCHECKED_CAST")
val JSON_INT_LIST_STR = SimpleModule().addDeserializer(List::class.java, object : JsonDeserializer<List<Integer>>() {
    override fun deserialize(parser: JsonParser, context: DeserializationContext) =
        try {
            val text = parser.text.trim('[', ']')
            if (text.isEmpty()) emptyList()
            else text.split(',').map { it.trim().toInt() } as List<Integer>
        } catch (e: Exception) {
            400 - "Invalid list value ${parser.text}: $e" }
})

val JACKSON_ARTEMIS = JACKSON.copy().apply {
    registerModule(JSON_INT_LIST_STR)
}