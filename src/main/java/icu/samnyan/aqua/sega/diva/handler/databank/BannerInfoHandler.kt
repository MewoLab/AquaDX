package icu.samnyan.aqua.sega.diva.handler.databank

import icu.samnyan.aqua.sega.diva.handler.BaseHandler
import icu.samnyan.aqua.sega.diva.model.request.BaseRequest
import icu.samnyan.aqua.sega.diva.model.response.databank.BannerInfoResponse
import org.springframework.stereotype.Component

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
class BannerInfoHandler {
    fun handle(request: BaseRequest) = BannerInfoResponse(
        request.cmd,
        request.req_id,
        "ok",
        null,
        null,
        null,
        null,
        null
    )
}
