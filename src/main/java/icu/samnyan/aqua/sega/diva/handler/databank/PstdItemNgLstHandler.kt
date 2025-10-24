package icu.samnyan.aqua.sega.diva.handler.databank

import icu.samnyan.aqua.sega.diva.handler.BaseHandler
import icu.samnyan.aqua.sega.diva.model.request.BaseRequest
import icu.samnyan.aqua.sega.diva.model.response.databank.PstdItemNgLstResponse
import icu.samnyan.aqua.sega.diva.util.DivaDateTimeUtil
import org.springframework.stereotype.Component
import java.time.LocalDateTime

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
class PstdItemNgLstHandler {
    fun handle(request: BaseRequest) = PstdItemNgLstResponse(
        request.cmd,
        request.req_id,
        "ok",
        DivaDateTimeUtil.getString(LocalDateTime.now()),
        "***",
        "***"
    )
}
