package icu.samnyan.aqua.net.games.mai2

import ext.API
import ext.RB
import ext.RP
import ext.minus
import icu.samnyan.aqua.net.db.AquaUserServices
import icu.samnyan.aqua.net.utils.SUCCESS
import icu.samnyan.aqua.sega.maimai2.model.Mai2Repos
import icu.samnyan.aqua.sega.maimai2.model.userdata.Mai2UserMusicDetail
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@API("api/v2/game/mai2")
class Mai2MusicDetailImport(
    val us: AquaUserServices,
    val repos: Mai2Repos,
) {
    @PostMapping("import-music-detail")
    suspend fun importMusicDetail(@RP token: String, @RB data: List<Mai2UserMusicDetail>) = us.jwt.auth(token) { u ->
        us.cardByName(u.username) { card ->
            val user = repos.userData.findByCardExtId(card.extId).orElse(null) ?: (404 - "User not found")
            data.forEach { newMusic ->
                val musicRec = repos.userMusicDetail.findByUserAndMusicIdAndLevel(user, newMusic.musicId, newMusic.level)
                if (musicRec.isPresent) {
                    val music = musicRec.get()
                    newMusic.apply {
                        id = music.id
                        this.user = user
                        achievement = achievement.coerceAtLeast(music.achievement)
                        scoreRank = scoreRank.coerceAtLeast(music.scoreRank)
                        comboStatus = comboStatus.coerceAtLeast(music.comboStatus)
                        syncStatus = syncStatus.coerceAtLeast(music.syncStatus)
                        deluxscoreMax = deluxscoreMax.coerceAtLeast(music.deluxscoreMax)
                        playCount = playCount.coerceAtLeast(music.playCount)
                    }
                } else {
                    newMusic.apply {
                        this.user = user
                    }
                }
            }
            repos.userMusicDetail.saveAll(data)
            SUCCESS
        }
    }
}
