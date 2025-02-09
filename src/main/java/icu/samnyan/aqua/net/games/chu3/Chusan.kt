package icu.samnyan.aqua.net.games.chu3

import ext.*
import icu.samnyan.aqua.net.db.AquaUserServices
import icu.samnyan.aqua.net.games.*
import icu.samnyan.aqua.net.utils.*
import icu.samnyan.aqua.sega.chusan.model.*
import icu.samnyan.aqua.sega.chusan.model.userdata.Chu3UserData
import org.springframework.web.bind.annotation.RestController

@RestController
@API("api/v2/game/chu3")
class Chusan(
    override val us: AquaUserServices,
    override val playlogRepo: Chu3UserPlaylogRepo,
    override val userDataRepo: Chu3UserDataRepo,
    override val userMusicRepo: Chu3UserMusicDetailRepo,
    val rp: Chu3Repos
): GameApiController<Chu3UserData>("chu3", Chu3UserData::class) {
    override suspend fun trend(@RP username: Str): List<TrendOut> = us.cardByName(username) { card ->
        findTrend(playlogRepo.findByUserCardExtId(card.extId)
            .map { TrendLog(it.playDate.toString(), it.playerRating) })
    }

    // Only show > AAA rank
    override val shownRanks = chu3Scores.filter { it.first >= 95 * 10000 }
    override val settableFields: Map<String, (Chu3UserData, String) -> Unit> by lazy { mapOf(
        "userName" to usernameCheck(SEGA_USERNAME_CAHRS),
        "nameplateId" to { u, v -> u.nameplateId = v.int },
        "frameId" to { u, v -> u.frameId = v.int },
        "trophyId" to { u, v -> u.trophyId = v.int },
        "mapIconId" to { u, v -> u.mapIconId = v.int },
        "voiceId" to { u, v -> u.voiceId = v.int },
        "avatarWear" to { u, v -> u.avatarWear = v.int },
        "avatarHead" to { u, v -> u.avatarHead = v.int },
        "avatarFace" to { u, v -> u.avatarFace = v.int },
        "avatarSkin" to { u, v -> u.avatarSkin = v.int },
        "avatarItem" to { u, v -> u.avatarItem = v.int },
        "avatarFront" to { u, v -> u.avatarFront = v.int },
        "avatarBack" to { u, v -> u.avatarBack = v.int },
    ) }
    override val gettableFields: Set<String> = setOf("level", "playerRating", "characterId")

    override suspend fun userSummary(@RP username: Str, @RP token: String?) = us.cardByName(username) { card ->
        // Summary values: total plays, player rating, server-wide ranking
        // number of each rank, max combo, number of full combo, number of all perfect
        val extra = rp.userGeneralData.findByUser_Card_ExtId(card.extId)
            .associate { it.propertyKey to it.propertyValue }

        val ratingComposition = mapOf(
            "recent10" to (extra["recent_rating_list"] ?: ""),
            "best30" to (extra["rating_base_list"] ?: ""),
            "hot10" to (extra["rating_hot_list"] ?: ""),
            "next10" to (extra["rating_next_list"] ?: ""),
        )

        genericUserSummary(card, ratingComposition)
    }

    /**
     * Added by Clansty for Bot
     * TODO: Reduce redundant code by combining with user-summary and user-music-from-list
     */
    @API("user-rating")
    suspend fun userRating(@RP username: Str) = us.cardByName(username) { card ->
        val extra = rp.userGeneralData.findByUser_Card_ExtId(card.extId)
            .associate { it.propertyKey to it.propertyValue }
        val best30Str = extra["rating_base_list"] ?: (400 - "No rating found")
        val recent10Str = extra["recent_rating_list"] ?: (400 - "No rating found")

        val best30 = best30Str.split(',').filterNot { it.isBlank() }.map { it.split(':') }
        val recent10 = recent10Str.split(',').filterNot { it.isBlank() }.map { it.split(':') }

        val musicIdList = listOf(
            best30.map { it[0].toInt() },
            recent10.map { it[0].toInt() },
        ).flatten()

        val userMusicList = rp.userMusicDetail.findByUser_Card_ExtIdAndMusicIdIn(card.extId, musicIdList)

        // Dont leak extId
        mapOf(
            "best30" to best30,
            "recent10" to recent10,
            "musicList" to userMusicList,
        )
    }

    // UserBox related APIs
    @API("user-box")
    fun userBox(@RP token: String) = us.jwt.auth(token) {
        val u = userDataRepo.findByCard(it.ghostCard) ?: (404 - "Game data not found")
        mapOf("user" to u, "items" to rp.userItem.findAllByUser(u))
    }
}
