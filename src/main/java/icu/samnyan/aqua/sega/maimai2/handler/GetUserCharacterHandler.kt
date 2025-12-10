package icu.samnyan.aqua.sega.maimai2.handler

import ext.int
import ext.logger
import icu.samnyan.aqua.net.games.mai2.Maimai2
import icu.samnyan.aqua.sega.general.BaseHandler
import icu.samnyan.aqua.sega.general.dao.CardRepository
import icu.samnyan.aqua.sega.maimai2.model.Mai2Repos
import icu.samnyan.aqua.sega.maimai2.model.userdata.Mai2ItemKind
import icu.samnyan.aqua.sega.maimai2.model.userdata.Mai2UserCharacter
import org.springframework.stereotype.Component
import kotlin.jvm.optionals.getOrNull

@Component("Maimai2GetUserCharacterHandler")
class GetUserCharacterHandler(
    val repos: Mai2Repos,
    val maimai2: Maimai2,
    val cardRepo: CardRepository,
) : BaseHandler {
    val charaIds = maimai2.itemMapping[Mai2ItemKind.chara.name]?.map { it.key.int() } ?: emptyList()

    init {
        if (charaIds.isEmpty()) logger.warn("Mai2 item info is empty")
    }

    override fun handle(request: Map<String, Any>): Any {
        val userId = (request["userId"] as Number).toLong()
        val gameOptions = cardRepo.findByExtId(userId).getOrNull()?.aquaUser?.gameOptions
        val userCharacterList = repos.userCharacter.findByUser_Card_ExtId(userId)
            .let { if (gameOptions?.mai2UnlockChara != true) it else (
                       charaIds.associateWith { Mai2UserCharacter().apply { characterId = it; level = 1 } } +
                       it.associateBy { it.characterId }
                   ).values }
            .let { if (gameOptions?.mai2UnlockCharaMaxLevel != true) it else it.map { it.apply { level = 9999 } } }

        return mapOf(
            "userId" to userId,
            "userCharacterList" to userCharacterList
        )
    }

    companion object {
        val logger = logger()
    }
}
