package icu.samnyan.aqua.sega.diva.handler.databank

import icu.samnyan.aqua.sega.diva.model.request.BaseRequest
import icu.samnyan.aqua.sega.diva.model.response.databank.RmtWpLstResponse
import icu.samnyan.aqua.sega.diva.util.DivaDateTimeUtil
import org.springframework.stereotype.Component
import java.time.LocalDateTime

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
class RmtWpLstHandler {
    fun handle(request: BaseRequest) = RmtWpLstResponse(
        DivaDateTimeUtil.getString(LocalDateTime.now()),
        "***"
    )
}
