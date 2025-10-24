package icu.samnyan.aqua.sega.diva.handler.databank

import icu.samnyan.aqua.sega.diva.handler.BaseHandler
import icu.samnyan.aqua.sega.diva.model.request.BaseRequest
import icu.samnyan.aqua.sega.diva.model.response.databank.PvNgMdlLstResponse
import icu.samnyan.aqua.sega.diva.util.DivaDateTimeUtil
import org.springframework.stereotype.Component
import java.time.LocalDateTime

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
class PvNgMdlLstHandler {
    fun handle(request: BaseRequest) = PvNgMdlLstResponse(
        request.cmd,
        request.req_id,
        "ok",
        DivaDateTimeUtil.getString(LocalDateTime.now()),
        "***"
    )
}
