package icu.samnyan.aqua.net.games.diva

import ext.*
import icu.samnyan.aqua.net.db.AquaUserServices
import icu.samnyan.aqua.sega.diva.DivaRepos
import org.springframework.web.bind.annotation.RestController

@RestController
@API("api/v2/game/diva")
class DivaExport(
    val us: AquaUserServices,
    val repos: DivaRepos,
) {
    @API("export")
    fun exportUserData(@RP token: Str) = us.jwt.auth(token) { account ->
        val profile = repos.profile.findByPdId(account.ghostCard.extId).orElse(null)
            ?: (404 - "User not found")

        linkedMapOf(
            "gameId" to "SBZV",
            "userData" to profile,
            "gameSession" to repos.gameSession.findByPdId(profile).orElse(null),
            "playLogList" to repos.playLog.findByPdId(profile),
            "playerContestList" to repos.contest.findByPdId(profile),
            "playerCustomizeList" to repos.customize.findByPdId(profile),
            "playerInventoryList" to repos.inventory.findByPdId(profile),
            "playerModuleList" to repos.module.findByPdId(profile),
            "playerPvCustomizeList" to repos.pvCustomize.findByPdId(profile),
            "playerPvRecordList" to repos.pvRecord.findByPdId(profile),
            "playerScreenShotList" to repos.screenShot.findByPdId(profile),
        )
    }
}
