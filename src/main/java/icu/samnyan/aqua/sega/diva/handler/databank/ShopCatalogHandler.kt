package icu.samnyan.aqua.sega.diva.handler.databank

import icu.samnyan.aqua.sega.diva.dao.gamedata.DivaModuleRepository
import icu.samnyan.aqua.sega.diva.handler.BaseHandler
import icu.samnyan.aqua.sega.diva.model.request.BaseRequest
import icu.samnyan.aqua.sega.diva.model.response.databank.ShopCatalogResponse
import icu.samnyan.aqua.sega.diva.util.URIEncoder.encode
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.time.LocalDateTime

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
class ShopCatalogHandler(private val moduleRepository: DivaModuleRepository) : BaseHandler() {
    fun handle(request: BaseRequest): Any {
        val moduleList = moduleRepository.findAll()

        return ShopCatalogResponse(
            request.cmd,
            request.req_id,
            "ok",
            LocalDateTime.now(),
            encode(moduleList.map { it.toInternal() }.joinToString(",") { encode(it) })
        )
    }
}
