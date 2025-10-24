package icu.samnyan.aqua.sega.diva.handler.databank

import icu.samnyan.aqua.sega.diva.handler.BaseHandler
import icu.samnyan.aqua.sega.diva.model.request.databank.BannerDataRequest
import icu.samnyan.aqua.sega.diva.model.response.databank.BannerDataResponse
import org.springframework.stereotype.Component
import java.time.LocalDateTime

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
class BannerDataHandler : BaseHandler() {
    fun handle(request: BannerDataRequest) = BannerDataResponse(
        request.cmd,
        request.req_id,
        "ok",
        LocalDateTime.now(),
        "***",
        "***",
        request.bd_id
    )
}
