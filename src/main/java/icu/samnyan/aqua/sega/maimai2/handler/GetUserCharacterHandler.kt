package icu.samnyan.aqua.sega.maimai2.handler

import ext.logger
import icu.samnyan.aqua.net.games.mai2.Maimai2
import icu.samnyan.aqua.sega.general.BaseHandler
import icu.samnyan.aqua.sega.general.dao.CardRepository
import icu.samnyan.aqua.sega.maimai2.model.Mai2Repos
import icu.samnyan.aqua.sega.maimai2.model.userdata.Mai2ItemKind
import org.springframework.stereotype.Component
import kotlin.jvm.optionals.getOrNull

@Component("Maimai2GetUserCharacterHandler")
class GetUserCharacterHandler(
    val repos: Mai2Repos,
    val maimai2: Maimai2,
    val cardRepo: CardRepository,
) : BaseHandler {
    val itemUnlock = maimai2.itemMapping[Mai2ItemKind.chara.name]?.map { mapOf(
        "characterId" to it.key,
        "level" to 9999,
        "awakening" to 1,
        "useCount" to 0
    ) }

    init {
        if (itemUnlock.isNullOrEmpty()) logger.warn("Mai2 item info is empty")
    }

    override fun handle(request: Map<String, Any>): Any {
        val userId = (request["userId"] as Number).toLong()

        // Aqua Net game unlock feature
        cardRepo.findByExtId(userId).getOrNull()?.aquaUser?.gameOptions?.let { opt ->
            if (!opt.unlockChara or itemUnlock.isNullOrEmpty()) return@let

            logger.info("Response: ${itemUnlock!!.size} Characters - All unlock")
            return mapOf(
                "userId" to userId,
                "userCharacterList" to itemUnlock
            )
        }

        return mapOf(
            "userId" to userId,
            "userCharacterList" to repos.userCharacter.findByUser_Card_ExtId(userId)
        )
    }

    companion object {
        val logger = logger()
    }
}
