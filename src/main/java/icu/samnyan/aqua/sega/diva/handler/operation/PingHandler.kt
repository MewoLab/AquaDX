package icu.samnyan.aqua.sega.diva.handler.operation

import ext.logger
import icu.samnyan.aqua.sega.diva.handler.BaseHandler
import icu.samnyan.aqua.sega.diva.model.request.BaseRequest
import icu.samnyan.aqua.sega.diva.model.response.operation.PingResponse
import icu.samnyan.aqua.sega.general.dao.PropertyEntryRepository
import icu.samnyan.aqua.sega.general.model.PropertyEntry
import org.springframework.stereotype.Component

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
class PingHandler(val rp: PropertyEntryRepository) : BaseHandler() {
    val logger = logger()

    fun handle(request: BaseRequest): String? {
        val news: PropertyEntry = rp.findByPropertyKey("diva_news") ?: PropertyEntry("diva_news", "xxx")
        val warning: PropertyEntry = rp.findByPropertyKey("diva_warning") ?: PropertyEntry("diva_warning", "xxx")

        val response = PingResponse(
            request.cmd,
            request.req_id,
            "ok",
            news.propertyValue,
            warning.propertyValue
        )

        val resp = this.build(mapper.toMap(response))
        logger.info("Response: {}", resp)

        return resp
    }
}
