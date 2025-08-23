package icu.samnyan.aqua.net.games.wacca

import ext.*
import icu.samnyan.aqua.net.db.AquaUserServices
import icu.samnyan.aqua.net.games.*
import icu.samnyan.aqua.net.utils.waccaScores
import icu.samnyan.aqua.sega.wacca.model.db.WaccaUser
import icu.samnyan.aqua.sega.wacca.model.db.WcUserBestScoreRepo
import icu.samnyan.aqua.sega.wacca.model.db.WcUserPlayLogRepo
import icu.samnyan.aqua.sega.wacca.model.db.WcUserRepo
import org.springframework.web.bind.annotation.RestController

@RestController
@API("api/v2/game/wacca")
class Wacca(
    override val us: AquaUserServices,
    override val playlogRepo: WcUserPlayLogRepo,
    override val userDataRepo: WcUserRepo,
    override val userMusicRepo: WcUserBestScoreRepo,
): GameApiController<WaccaUser>("wacca", WaccaUser::class) {
    override val settableFields: Map<String, (WaccaUser, String) -> Unit> by lazy { mapOf(
        "userName" to usernameCheck(WACCA_USERNAME_CHARS),

        "lastRomVersion" to { u, v -> u.lastRomVersion = v },
    ) }

    override suspend fun trend(@RP username: String) = us.cardByName(username) { card ->
        findTrend(playlogRepo.findByUserCardExtId(card.extId)
            .map { TrendLog(it.userPlayDate.utc().isoDate(), it.afterRating) })
    }

    override suspend fun userSummary(@RP username: String, @RP token: String?) = us.cardByName(username) { card ->
        // TODO: Rating composition

        val data = userDataRepo.findByCard_ExtId(card.extId)

        genericUserSummary(card, mapOf(), null, if (data.isPresent) data.get().favoriteSongs else null)
    }

    override val shownRanks: List<Pair<Int, String>> = waccaScores.filter { it.first > 85 * 10000 }
}
