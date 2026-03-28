package icu.samnyan.aqua.sega.diva.handler.databank

import icu.samnyan.aqua.sega.diva.DivaModuleRepository
import icu.samnyan.aqua.sega.diva.model.BaseRequest
import icu.samnyan.aqua.sega.diva.model.ShopCatalogResponse
import icu.samnyan.aqua.sega.diva.util.URIEncoder.encode
import org.springframework.stereotype.Component
import java.time.LocalDateTime

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
class ShopCatalogHandler(private val moduleRepository: DivaModuleRepository) {
    fun handle(request: BaseRequest): Any {
        val moduleList = moduleRepository.findAll()

        return ShopCatalogResponse(
            LocalDateTime.now(),
            encode(moduleList.map { it.toInternal() }.joinToString(",") { encode(it) })
        )
    }
}
