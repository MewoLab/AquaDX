package icu.samnyan.aqua.sega.diva.handler.databank

import icu.samnyan.aqua.sega.diva.model.request.BaseRequest
import icu.samnyan.aqua.sega.diva.model.response.databank.PvDefChrLstResponse
import icu.samnyan.aqua.sega.diva.util.DivaDateTimeUtil
import org.springframework.stereotype.Component
import java.time.LocalDateTime

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
class PvDefChrLstHandler {
    fun handle(request: BaseRequest) = PvDefChrLstResponse(
        DivaDateTimeUtil.getString(LocalDateTime.now()),
        "***"
    )
}
