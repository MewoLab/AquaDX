package icu.samnyan.aqua.sega.diva.handler.databank

import icu.samnyan.aqua.sega.diva.handler.BaseHandler
import icu.samnyan.aqua.sega.diva.model.request.BaseRequest
import icu.samnyan.aqua.sega.diva.model.response.databank.NvRankingResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
class NvRankingHandler : BaseHandler() {
    fun handle(request: BaseRequest): Any {
        val response = NvRankingResponse(
            request.cmd,
            request.req_id,
            "ok",
            null,
            null,
            null,
            null
        )

        val resp = this.build(mapper.toMap(response))
        logger.info("Response: {}", resp)

        return resp
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(NvRankingHandler::class.java)
    }
}
