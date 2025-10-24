package icu.samnyan.aqua.sega.diva.handler.databank

import icu.samnyan.aqua.sega.diva.dao.gamedata.PvEntryRepository
import icu.samnyan.aqua.sega.diva.handler.BaseHandler
import icu.samnyan.aqua.sega.diva.model.common.Difficulty
import icu.samnyan.aqua.sega.diva.model.gamedata.PvEntry
import icu.samnyan.aqua.sega.diva.model.request.BaseRequest
import icu.samnyan.aqua.sega.diva.model.response.databank.PvListResponse
import icu.samnyan.aqua.sega.diva.util.URIEncoder.encode
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.function.Consumer

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
class PvListHandler(private val pvEntryRepository: PvEntryRepository) : BaseHandler() {
    private val df: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    fun handle(request: BaseRequest): Any {
        val sb = StringBuilder()

        val easyList = pvEntryRepository.findByDifficulty(Difficulty.EASY)
        val normalList = pvEntryRepository.findByDifficulty(Difficulty.NORMAL)
        val hardList = pvEntryRepository.findByDifficulty(Difficulty.HARD)
        val extremeList = pvEntryRepository.findByDifficulty(Difficulty.EXTREME)

        sb.append(encode(difficultyString(easyList))).append(",")
        sb.append(encode(difficultyString(normalList))).append(",")
        sb.append(encode(difficultyString(hardList))).append(",")
        sb.append(encode(difficultyString(extremeList))).append(",")
        sb.append("%2A%2A%2A")

        val response = PvListResponse(
            request.cmd,
            request.req_id,
            "ok",
            LocalDateTime.now(),
            sb.toString()
        )

        val resp = this.build(mapper.toMap(response))
        logger.info("Response: {}", resp)

        return resp
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

    private fun difficultyString(list: MutableList<PvEntry?>): String {
        val sb = StringBuilder()
        list.forEach(Consumer { x: PvEntry? -> sb.append(encode(entryString(x!!))).append(",") })
        if (sb.length > 0) sb.deleteCharAt(sb.length - 1)
        return sb.toString()
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(BannerDataHandler::class.java)
    }
}
