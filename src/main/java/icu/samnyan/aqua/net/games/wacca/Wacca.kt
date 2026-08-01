package icu.samnyan.aqua.net.games.wacca

import ext.*
import icu.samnyan.aqua.net.db.AquaUserServices
import icu.samnyan.aqua.net.games.*
import icu.samnyan.aqua.net.utils.waccaScores
import icu.samnyan.aqua.sega.wacca.model.db.*
import org.springframework.web.bind.annotation.RestController

@RestController
@API("api/v2/game/wacca")
class Wacca(
    override val us: AquaUserServices,
    override val playlogRepo: WcUserPlayLogRepo,
    override val userDataRepo: WcUserRepo,
    override val userMusicRepo: WcUserBestScoreRepo,
    val repos: WaccaRepos,
): GameApiController<WaccaUser>("wacca", WaccaUser::class) {
    override val settableFields: Map<String, (WaccaUser, String) -> Unit> by lazy { mapOf(
        "userName" to usernameCheck(true),

        "lastRomVersion" to { u, v -> u.lastRomVersion = v },
    ) }

    override suspend fun trend(@RP username: String) = us.cardByName(username) { card ->
        findTrend(playlogRepo.findByUserCardExtId(card.extId)
            .map { TrendLog(it.userPlayDate.utc().isoDate(), it.afterRating) })
    }

    override suspend fun userSummary(@RP username: String, @RP token: String?) = us.cardByName(username) { card ->
        // TODO: Rating composition

        val data = userDataRepo.findByCard_ExtId(card.extId)

        genericUserSummary(card, mapOf(), null, data?.favoriteSongs)
    }

    @API("export")
    fun exportUserData(@RP token: Str) = us.jwt.auth(token) { account ->
        val user = repos.user.findByCard(account.ghostCard) ?: (404 - "User not found")
        WaccaDataExport(
            userData = user,
            userOptionList = repos.option.findByUser(user),
            userBingoList = repos.bingo.findByUser(user),
            userFriendList = repos.friend.findByUser(user).map {
                WaccaFriendExport(it.with.id, it.isAccepted)
            },
            userGateList = repos.gate.findByUser(user),
            userItemList = repos.item.findByUser(user),
            userBestScoreList = repos.bestScore.findByUser(user),
            userPlaylogList = repos.playLog.findByUser(user),
            userStageUpList = repos.stageUp.findByUser(user),
        )
    }

    override val shownRanks: List<Pair<Int, String>> = waccaScores.filter { it.first > 85 * 10000 }
}

data class WaccaFriendExport(val withUserId: Long, val isAccepted: Boolean)

data class WaccaDataExport(
    val gameId: String = "SDFE",
    val userData: WaccaUser,
    val userOptionList: List<WcUserOption>,
    val userBingoList: List<WcUserBingo>,
    val userFriendList: List<WaccaFriendExport>,
    val userGateList: List<WcUserGate>,
    val userItemList: List<WcUserItem>,
    val userBestScoreList: List<WcUserScore>,
    val userPlaylogList: List<WcUserPlayLog>,
    val userStageUpList: List<WcUserStageUp>,
)
