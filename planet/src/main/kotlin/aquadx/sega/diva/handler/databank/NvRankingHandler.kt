package aquadx.sega.diva.handler.databank

import aquadx.sega.diva.model.BaseRequest
import aquadx.sega.diva.model.NvRankingResponse
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
