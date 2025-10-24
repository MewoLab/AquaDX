package icu.samnyan.aqua.sega.diva.handler.databank

import icu.samnyan.aqua.sega.diva.dao.gamedata.FestaRepository
import icu.samnyan.aqua.sega.diva.handler.BaseHandler
import icu.samnyan.aqua.sega.diva.model.common.collection.FestaCollection
import icu.samnyan.aqua.sega.diva.model.request.BaseRequest
import icu.samnyan.aqua.sega.diva.model.response.databank.FestaInfoResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
class FestaInfoHandler(private val festaRepository: FestaRepository) : BaseHandler() {
    fun handle(request: BaseRequest): Any {
        val festaList = festaRepository.findTop2ByEnableOrderByCreateDateDesc(true)
        val collection = FestaCollection(festaList)

        return FestaInfoResponse(
            request.cmd,
            request.req_id,
            "ok",
            collection.ids,
            collection.names,
            collection.kinds,
            collection.diffs,
            collection.pvIds,
            collection.attr,
            collection.addVps,
            collection.vpMultipliers,
            collection.starts,
            collection.ends,
            collection.lastUpdateTime
        )
    }
}
