package icu.samnyan.aqua.net.games.mai2

import ext.*
import icu.samnyan.aqua.net.db.AquaUserServices
import icu.samnyan.aqua.net.games.*
import icu.samnyan.aqua.net.utils.*
import icu.samnyan.aqua.sega.maimai2.model.*
import icu.samnyan.aqua.sega.maimai2.model.userdata.*
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@API("api/v2/game/mai2")
class Maimai2(
    override val us: AquaUserServices,
    override val playlogRepo: Mai2UserPlaylogRepo,
    override val userDataRepo: Mai2UserDataRepo,
    override val userMusicRepo: Mai2UserMusicDetailRepo,
    val repos: Mai2Repos,
) : GameApiController<Mai2UserDetail>("mai2", Mai2UserDetail::class) {
    override suspend fun trend(@RP username: Str): List<TrendOut> = us.cardByName(username) { card ->
        findTrend(playlogRepo.findByUserCardExtId(card.extId)
            .map { TrendLog(it.playDate, it.afterRating) })
    }

    // Only show > S rank
    override val shownRanks = mai2Scores.filter { it.first >= 97 * 10000 }
    override val settableFields: Map<String, (Mai2UserDetail, String) -> Unit> by lazy {
        mapOf("userName" to usernameCheck(SEGA_USERNAME_CAHRS))
    }

    override suspend fun userSummary(@RP username: Str, @RP token: String?) = us.cardByName(username) { card ->
        val extra = repos.userGeneralData.findByUser_Card_ExtId(card.extId)
            .associate { it.propertyKey to it.propertyValue }

        val ratingComposition = mapOf(
            "best35" to (extra["recent_rating"] ?: ""),
            "best15" to (extra["recent_rating_new"] ?: "")
        )

        // if isLogin than boolean or null
        // use the type to check user login in frontend
        val isMyRival = token?.let { t ->
            us.jwt.auth(t) { u ->
                if (u.username == username) return@auth null
                us.cardByName(u.username) { myCard ->
                    val user = repos.userData.findByCardExtId(card.extId).orElse(null) ?: (404 - "User not found")
                    val myRival = repos.userGeneralData.findByUser_Card_ExtIdAndPropertyKey(myCard.extId, "favorite_rival")
                        .map { it.propertyValue.split(',') }.orElse(emptyList()).filter { it.isNotEmpty() }.map { it.long() }
                    myRival.contains(user.id)
                }
            }
        }

        genericUserSummary(card, ratingComposition, isMyRival)
    }

    @API("user-rating")
    suspend fun userRating(@RP username: Str) = us.cardByName(username) { card ->
        val extra = repos.userGeneralData.findByUser_Card_ExtId(card.extId)
            .associate { it.propertyKey to it.propertyValue }
        val b35Str = extra["recent_rating"] ?: (400 - "No rating found")
        val b15Str = extra["recent_rating_new"] ?: (400 - "No rating found")

        val b35 = b35Str.split(',').filterNot { it.isBlank() }.map { it.split(':') }
        val b15 = b15Str.split(',').filterNot { it.isBlank() }.map { it.split(':') }

        val musicIdList = listOf(
            b35.map { it[0].toInt() },
            b15.map { it[0].toInt() },
        ).flatten()

        val userMusicList = repos.userMusicDetail.findByUser_Card_ExtIdAndMusicIdIn(card.extId, musicIdList)

        // Dont leak extId
        mapOf(
            "best35" to b35,
            "best15" to b15,
            "musicList" to userMusicList,
        )
    }

    @API("user-name-plate")
    suspend fun userNamePlate(@RP username: Str) = us.cardByName(username) { card ->
        val userData = repos.userData.findByCardExtId(card.extId).orElse(null) ?: (404 - "User not found")
        mapOf(
            "iconId" to userData.iconId,
            "plateId" to userData.plateId,
            "titleId" to userData.titleId,
            "classRank" to userData.classRank,
            "playerRating" to userData.playerRating,
            "userName" to userData.userName,
            "courseRank" to userData.courseRank,
        )
    }

    @API("user-favorite")
    suspend fun userFavorite(@RP username: Str) = us.cardByName(username) { card ->
        repos.userFavorite.findByUser_Card_ExtId(card.extId)
    }

    @PostMapping("change-name")
    suspend fun changeName(@RP token: String, @RP newName: String) = us.jwt.auth(token) { u ->
        val newNameFull = toFullWidth(newName)
        us.cardByName(u.username) { card ->
            val user = userDataRepo.findByCard(card) ?: (404 - "User not found")
            user.userName = newNameFull
            userDataRepo.save(user)
        }
        mapOf("newName" to newNameFull)
    }

    @PostMapping("set-rival")
    suspend fun setRival(@RP token: String, @RP rivalUserName: String, @RP isAdd: Boolean) = us.jwt.auth(token) { u ->
        us.cardByName(u.username) { myCard ->
            val rivalCard = us.cardByName(rivalUserName) { it }
            val rivalUser = repos.userData.findByCardExtId(rivalCard.extId).orElse(null) ?: (404 - "User not found")
            val myRival = repos.userGeneralData.findByUser_Card_ExtIdAndPropertyKey(myCard.extId, "favorite_rival").orElse(null)
                ?: Mai2UserGeneralData().apply {
                    user = repos.userData.findByCardExtId(myCard.extId).orElse(null) ?: (404 - "User not found")
                    propertyKey = "favorite_rival"
                }
            val myRivalList = myRival.propertyValue.split(',').filter { it.isNotEmpty() }.mut

            if (isAdd && myRivalList.size >= 4) {
                (400 - "Rival list is full")
            } else if (isAdd) {
                myRivalList.add(rivalUser.id.toString())
            } else {
                myRivalList.remove(rivalUser.id.toString())
            }

            myRival.propertyValue = myRivalList.joinToString(",")
            repos.userGeneralData.save(myRival)
        }
        SUCCESS
    }
}
