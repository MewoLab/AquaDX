package icu.samnyan.aqua.sega.diva.handler.databank

import icu.samnyan.aqua.sega.diva.DivaRepos
import icu.samnyan.aqua.sega.diva.model.BaseRequest
import icu.samnyan.aqua.sega.diva.model.ContestInfoResponse
import icu.samnyan.aqua.sega.diva.model.db.gamedata.Contest
import icu.samnyan.aqua.sega.diva.util.URIEncoder.encode
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.function.Consumer
import kotlin.math.max

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
class ContestInfoHandler(val db: DivaRepos) {
    fun handle(request: BaseRequest): Any {
        val contestList = db.g.contest.findTop8ByEnable(true)
        var ci_str = "***"
        if (!contestList.isEmpty()) {
            val sb = StringBuilder()
            contestList.forEach(Consumer { x: Contest? -> sb.append(encode(x!!.string)).append(",") })
            sb.append("%2A%2A%2A,".repeat(max(0, 8 - contestList.size)))
            sb.deleteCharAt(sb.length - 1)
            ci_str = sb.toString()
        }
        return ContestInfoResponse(
            LocalDateTime.now(),
            ci_str
        )
    }
}
