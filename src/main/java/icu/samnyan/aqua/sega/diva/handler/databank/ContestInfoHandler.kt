package icu.samnyan.aqua.sega.diva.handler.databank

import icu.samnyan.aqua.sega.diva.dao.gamedata.ContestRepository
import icu.samnyan.aqua.sega.diva.handler.BaseHandler
import icu.samnyan.aqua.sega.diva.model.gamedata.Contest
import icu.samnyan.aqua.sega.diva.model.request.BaseRequest
import icu.samnyan.aqua.sega.diva.model.response.databank.ContestInfoResponse
import icu.samnyan.aqua.sega.diva.util.URIEncoder.encode
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.function.Consumer
import kotlin.math.max

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
class ContestInfoHandler(private val contestRepository: ContestRepository) : BaseHandler() {
    fun handle(request: BaseRequest): Any {
        val contestList = contestRepository.findTop8ByEnable(true)
        var ci_str = "***"
        if (!contestList.isEmpty()) {
            val sb = StringBuilder()
            contestList.forEach(Consumer { x: Contest? -> sb.append(encode(x!!.getString())).append(",") })
            sb.append("%2A%2A%2A,".repeat(max(0, 8 - contestList.size)))
            sb.deleteCharAt(sb.length - 1)
            ci_str = sb.toString()
        }
        return ContestInfoResponse(
            request.cmd,
            request.req_id,
            "ok",
            LocalDateTime.now(),
            ci_str
        )
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(CmPlyInfoHandler::class.java)
    }
}
