package icu.samnyan.aqua.sega.diva.handler.databank

import icu.samnyan.aqua.sega.diva.handler.BaseHandler
import icu.samnyan.aqua.sega.diva.model.request.BaseRequest
import icu.samnyan.aqua.sega.diva.model.response.databank.PstdItemNgLstResponse
import icu.samnyan.aqua.sega.diva.util.DivaDateTimeUtil
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.time.LocalDateTime

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
class PstdItemNgLstHandler : BaseHandler() {
    fun handle(request: BaseRequest): Any {
        val response = PstdItemNgLstResponse(
            request.cmd,
            request.req_id,
            "ok",
            DivaDateTimeUtil.getString(LocalDateTime.now()),
            "***",
            "***"
        )

        val resp = this.build(mapper.toMap(response))
        logger.info("Response: {}", resp)

        return resp
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(PstdItemNgLstHandler::class.java)
    }
}
