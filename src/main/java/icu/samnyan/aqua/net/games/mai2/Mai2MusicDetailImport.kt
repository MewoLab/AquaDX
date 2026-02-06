package icu.samnyan.aqua.net.games.mai2

import ext.API
import ext.RB
import ext.RP
import ext.minus
import icu.samnyan.aqua.net.db.AquaUserServices
import icu.samnyan.aqua.net.utils.SUCCESS
import icu.samnyan.aqua.sega.general.service.CardService
import icu.samnyan.aqua.sega.maimai2.model.Mai2Repos
import icu.samnyan.aqua.sega.maimai2.model.userdata.Mai2UserMusicDetail
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@API("api/v2/game/mai2")
class Mai2MusicDetailImport(
    val us: AquaUserServices,
    val repos: Mai2Repos,
    val cardService: CardService,
) {
    @PostMapping("import-music-detail")
    suspend fun importMusicDetail(@RP token: String, @RB data: List<Mai2UserMusicDetail>) = us.jwt.auth(token) { u ->
        us.cardByName(u.username) { card ->
            val user = repos.userData.findByCardExtId(card.extId) ?: (404 - "User not found")
            data.forEach { newMusic ->
                newMusic.user = user
                repos.userMusicDetail.findByUserAndMusicIdAndLevel(user, newMusic.musicId, newMusic.level)?.let { m ->
                    newMusic.apply {
                        id = m.id
                        achievement = achievement.coerceAtLeast(m.achievement)
                        scoreRank = scoreRank.coerceAtLeast(m.scoreRank)
                        comboStatus = comboStatus.coerceAtLeast(m.comboStatus)
                        syncStatus = syncStatus.coerceAtLeast(m.syncStatus)
                        deluxscoreMax = deluxscoreMax.coerceAtLeast(m.deluxscoreMax)
                        playCount = playCount.coerceAtLeast(m.playCount)
                    }
                }
            }
            repos.userMusicDetail.saveAll(data)
            cardService.updateCardTimestamp(card, "mai2")
            SUCCESS
        }
    }
}
