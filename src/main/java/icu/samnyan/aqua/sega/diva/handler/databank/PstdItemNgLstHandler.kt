package icu.samnyan.aqua.sega.diva.handler.databank

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
        DivaDateTimeUtil.getString(LocalDateTime.now()),
        "***",
        "***"
    )
}
