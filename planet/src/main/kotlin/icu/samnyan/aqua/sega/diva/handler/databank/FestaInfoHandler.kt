package icu.samnyan.aqua.sega.diva.handler.databank

import icu.samnyan.aqua.sega.diva.FestaRepository
import icu.samnyan.aqua.sega.diva.model.BaseRequest
import icu.samnyan.aqua.sega.diva.model.FestaInfoResponse
import icu.samnyan.aqua.sega.diva.model.common.collection.FestaCollection
import org.springframework.stereotype.Component

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
class FestaInfoHandler(private val festaRepository: FestaRepository) {
    fun handle(request: BaseRequest): Any {
        val festaList = festaRepository.findTop2ByEnableOrderByCreateDateDesc(true)
        val collection = FestaCollection(festaList)

        return FestaInfoResponse(
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
