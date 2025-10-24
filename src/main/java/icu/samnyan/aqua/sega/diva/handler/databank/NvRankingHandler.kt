package icu.samnyan.aqua.sega.diva.handler.databank

import icu.samnyan.aqua.sega.diva.handler.BaseHandler
import icu.samnyan.aqua.sega.diva.model.request.BaseRequest
import icu.samnyan.aqua.sega.diva.model.response.databank.NvRankingResponse
import org.springframework.stereotype.Component

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
class NvRankingHandler {
    fun handle(request: BaseRequest) = NvRankingResponse(
        request.cmd,
        request.req_id,
        "ok",
        null,
        null,
        null,
        null
    )
}
