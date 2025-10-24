package icu.samnyan.aqua.sega.diva.handler.databank

import icu.samnyan.aqua.sega.diva.model.request.BaseRequest
import icu.samnyan.aqua.sega.diva.model.response.BaseResponse
import org.springframework.stereotype.Component

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
class NgWordHandler {
    fun handle(request: BaseRequest) = BaseResponse(
        request.cmd,
        request.req_id,
        "ok"
    )
}
