package icu.samnyan.aqua.sega.diva.handler.databank

import icu.samnyan.aqua.sega.diva.dao.gamedata.DivaCustomizeRepository
import icu.samnyan.aqua.sega.diva.handler.BaseHandler
import icu.samnyan.aqua.sega.diva.model.request.BaseRequest
import icu.samnyan.aqua.sega.diva.model.response.databank.CstmzItmCtlgResponse
import icu.samnyan.aqua.sega.diva.util.DivaDateTimeUtil
import icu.samnyan.aqua.sega.diva.util.URIEncoder.encode
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.time.LocalDateTime

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
class CstmzItmCtlgHandler(private val customizeRepository: DivaCustomizeRepository) : BaseHandler() {
    fun handle(request: BaseRequest): Any {
        val customizeList = customizeRepository.findAll()

        return CstmzItmCtlgResponse(
            request.cmd,
            request.req_id,
            "ok",
            DivaDateTimeUtil.getString(LocalDateTime.now()),
            encode(customizeList.map { it.toInternal() }.joinToString(",") { encode(it) })
        )
    }
}
