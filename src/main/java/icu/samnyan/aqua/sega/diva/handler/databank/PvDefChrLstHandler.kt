package icu.samnyan.aqua.sega.diva.handler.databank

import icu.samnyan.aqua.sega.diva.handler.BaseHandler
import icu.samnyan.aqua.sega.diva.model.request.BaseRequest
import icu.samnyan.aqua.sega.diva.model.response.databank.PvDefChrLstResponse
import icu.samnyan.aqua.sega.diva.util.DivaDateTimeUtil
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.time.LocalDateTime

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
class PvDefChrLstHandler : BaseHandler() {
    fun handle(request: BaseRequest) = PvDefChrLstResponse(
        request.cmd,
        request.req_id,
        "ok",
        DivaDateTimeUtil.getString(LocalDateTime.now()),
        "***"
    )
}
