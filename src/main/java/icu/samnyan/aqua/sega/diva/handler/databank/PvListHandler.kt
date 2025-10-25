package icu.samnyan.aqua.sega.diva.handler.databank

import icu.samnyan.aqua.sega.diva.DivaRepos
import icu.samnyan.aqua.sega.diva.model.BaseRequest
import icu.samnyan.aqua.sega.diva.model.PvListResponse
import icu.samnyan.aqua.sega.diva.model.common.Difficulty
import icu.samnyan.aqua.sega.diva.model.db.gamedata.PvEntry
import icu.samnyan.aqua.sega.diva.util.URIEncoder.encode
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.function.Consumer

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
class PvListHandler(val db: DivaRepos) {
    private val df: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    fun handle(request: BaseRequest): Any {
        val sb = StringBuilder()

        val easyList = db.g.pvEntry.findByDifficulty(Difficulty.EASY)
        val normalList = db.g.pvEntry.findByDifficulty(Difficulty.NORMAL)
        val hardList = db.g.pvEntry.findByDifficulty(Difficulty.HARD)
        val extremeList = db.g.pvEntry.findByDifficulty(Difficulty.EXTREME)

        sb.append(encode(difficultyString(easyList))).append(",")
        sb.append(encode(difficultyString(normalList))).append(",")
        sb.append(encode(difficultyString(hardList))).append(",")
        sb.append(encode(difficultyString(extremeList))).append(",")
        sb.append("%2A%2A%2A")

        return PvListResponse(
            LocalDateTime.now(),
            sb.toString()
        )
    }

    private fun entryString(entry: PvEntry): String {
        return "" + entry.pvId + "," +
            entry.version + "," +
            entry.edition.value + "," +
            df.format(entry.demoStart) + "," +
            df.format(entry.demoEnd) + "," +
            df.format(entry.playableStart) + "," +
            df.format(entry.playableEnd)
    }

    private fun difficultyString(list: MutableList<PvEntry>): String {
        val sb = StringBuilder()
        list.forEach(Consumer { sb.append(encode(entryString(it))).append(",") })
        if (sb.isNotEmpty()) sb.deleteCharAt(sb.length - 1)
        return sb.toString()
    }
}
