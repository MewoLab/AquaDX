package icu.samnyan.aqua.sega.diva.handler.databank

import icu.samnyan.aqua.sega.diva.model.BaseRequest
import icu.samnyan.aqua.sega.diva.model.NvRankingResponse
import org.springframework.stereotype.Component

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
class NvRankingHandler {
    fun handle(request: BaseRequest) = NvRankingResponse(
        null,
        null,
        null,
        null
    )
}
