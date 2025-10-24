package icu.samnyan.aqua.sega.maimai2.handler

import icu.samnyan.aqua.sega.general.BaseHandler
import icu.samnyan.aqua.sega.maimai2.model.Mai2Repos
import org.springframework.stereotype.Component

@Component("Maimai2GetUserFavoriteItemHandler")
class GetUserFavoriteItemHandler(val repos: Mai2Repos) : BaseHandler {
    override fun handle(request: Map<String, Any>): Any {
        val userId = (request["userId"] as Number).toLong()
        val kind = (request["kind"] as Number).toInt()

        val items = when (kind) {
            1 -> repos.userGeneralData.findByUser_Card_ExtIdAndPropertyKey(userId, "favorite_music")
            2 -> repos.userGeneralData.findByUser_Card_ExtIdAndPropertyKey(userId, "favorite_rival")
            else -> null
        }?.let { fav ->
            val v = fav.propertyValue
            if (v.isNotBlank()) v.split(",").filter { it.isNotEmpty() }.mapIndexed { i, record ->
                mapOf("id" to record.toInt(), "orderId" to i) }
            else null
        } ?: emptyList()

        return mapOf(
            "userId" to userId,
            "kind" to kind,
            "length" to items.size,
            "nextIndex" to 0,
            "userFavoriteItemList" to items
        )
    }
}
