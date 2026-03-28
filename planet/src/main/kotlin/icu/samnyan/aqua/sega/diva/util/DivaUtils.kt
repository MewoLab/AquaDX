package icu.samnyan.aqua.sega.diva.util

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import icu.samnyan.aqua.sega.diva.PlayerPvRecordRepository
import icu.samnyan.aqua.sega.diva.model.common.Edition
import icu.samnyan.aqua.sega.diva.model.common.LevelInfo
import icu.samnyan.aqua.sega.diva.model.db.userdata.PlayerProfile
import org.springframework.stereotype.Component
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

object DivaStringUtils {
    @JvmStatic
    fun getDummyString(content: String, length: Int) = "$content,".repeat(length).removeSuffix(",")
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

@Component
class DivaCalculator(private val playerPvRecordRepository: PlayerPvRecordRepository) {
    fun getLevelInfo(profile: PlayerProfile): LevelInfo {
        val recordList = playerPvRecordRepository.findByPdIdAndEdition(profile, Edition.ORIGINAL)
        var totalAttain = 0
        for (record in recordList) {
            totalAttain += record.maxAttain
        }

        val level = totalAttain / 13979
        val exp = ((totalAttain % 13979) / 13979.0f * 100.0f).roundToInt()

        return LevelInfo(level + 1, exp)
    }
}

object URIEncoder {
    @JvmStatic
    fun encode(str: String) = URLEncoder.encode(str, StandardCharsets.UTF_8).replace("\\+".toRegex(), "%20")
}