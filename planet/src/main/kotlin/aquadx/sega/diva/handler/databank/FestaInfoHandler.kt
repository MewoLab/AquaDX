package aquadx.sega.diva.handler.databank

import aquadx.sega.diva.FestaRepository
import aquadx.sega.diva.model.BaseRequest
import aquadx.sega.diva.model.FestaInfoResponse
import aquadx.sega.diva.model.common.collection.FestaCollection
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
