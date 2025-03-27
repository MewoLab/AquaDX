package icu.samnyan.aqua.net.transfer

import ext.*
import icu.samnyan.aqua.sega.chusan.model.request.Chu3UserAll
import icu.samnyan.aqua.sega.chusan.model.userdata.Chu3UserActivity
import icu.samnyan.aqua.sega.chusan.model.userdata.Chu3UserItem
import icu.samnyan.aqua.sega.chusan.model.userdata.UserMusicDetail
import icu.samnyan.aqua.sega.maimai2.model.request.Mai2UserAll
import icu.samnyan.aqua.sega.maimai2.model.userdata.Mai2UserFavorite
import icu.samnyan.aqua.sega.maimai2.model.userdata.Mai2UserItem
import icu.samnyan.aqua.sega.maimai2.model.userdata.Mai2UserMusicDetail
import icu.samnyan.aqua.sega.ongeki.model.request.UpsertUserAll
import icu.samnyan.aqua.sega.ongeki.model.userdata.UserItem
import icu.samnyan.aqua.sega.util.jackson.BasicMapper
import icu.samnyan.aqua.sega.util.jackson.IMapper
import icu.samnyan.aqua.sega.util.jackson.StringMapper


abstract class DataBroker(
    val allNet: AllNetClient,
    val log: (String) -> Unit,
) {
    abstract val mapper: IMapper
    abstract val url: String

    inline fun <reified T> String.getNullable(key: String, data: JDict): T? = "$url/$this".request()
        .postZ(mapper.write(data))
        .bodyMaybeZ()
        ?.jsonMaybeMap()?.get(key)
        ?.let { mapper.convert<T>(it) }
        ?.also {
            if (it is List<*>) log("✅ $this: ${it.size}")
            else log("✅ $this")
        }

    inline fun <reified T> String.get(key: String, data: JDict): T = getNullable(key, data) ?: run {
        log("❌ $this")
        if (this == "GetUserDataApi") 404 - "Failed to get $this (User not found?)"
        else 417 - "Failed to get $this"
    }

    fun prePull(): Pair<Map<String, Long>, MutableMap<String, Long>> {
        log("Game URL: ${allNet.gameUrl}")
        log("User ID: ${allNet.userId}")

        val userId = mapOf("userId" to allNet.userId)
        val paged = userId + mapOf("nextIndex" to 0, "maxCount" to 10000000)
        return userId to paged
    }

    abstract fun pull(): String
    fun push(data: String) {
        log("Pushing data")
        "$url/UpsertUserAllApi".request().postZ(mapper.write(mapOf(
            "userId" to allNet.userId,
            "upsertUserAll" to data.jsonMap()
        ))).bodyMaybeZ()?.also { log(it) }
    }
}


class ChusanDataBroker(allNet: AllNetClient, log: (String) -> Unit): DataBroker(allNet, log) {
    override val mapper = StringMapper()
    override val url by lazy { "${allNet.gameUrl.ensureEndingSlash()}ChuniServlet" }

    class UserMusicWrapper(var userMusicDetailList: List<UserMusicDetail>)

    override fun pull(): String {
        val (userId, paged) = prePull()

        return mapper.write(Chu3UserAll().apply {
            userData = ls("GetUserDataApi".get("userData", userId))
            userGameOption = ls("GetUserOptionApi".get("userGameOption", userId))
            userCharacterList = "GetUserCharacterApi".get("userCharacterList", paged)
            userActivityList = (1..5).flatMap {
                "GetUserActivityApi".get<List<Chu3UserActivity>>("userActivityList", userId + mapOf("kind" to it))
            }
            userItemList = (1..12).flatMap {
                "GetUserItemApi".get<List<Chu3UserItem>>("userItemList", paged + mapOf("nextIndex" to 10000000000 * it))
            }
            userRecentRatingList = "GetUserRecentRatingApi".get("userRecentRatingList", userId)
            userMusicDetailList = "GetUserMusicApi".get<List<UserMusicWrapper>>("userMusicList", paged)
                .flatMap { it.userMusicDetailList }
            userCourseList = "GetUserCourseApi".get("userCourseList", paged)
            userFavoriteMusicList = "GetUserFavoriteItemApi".get("userFavoriteItemList", paged + mapOf("kind" to 1))
            // TODO userMapAreaList = "GetUserMapAreaApi"
            // TODO userNetBattleData = ls("GetUserNetBattleDataApi".get("userNetBattleData", userId))
            userUnlockChallengeList = "GetUserUCApi".get("userUnlockChallengeList", userId)
        })
    }
}


class MaimaiDataBroker(allNet: AllNetClient, log: (String) -> Unit): DataBroker(allNet, log) {
    override val mapper = BasicMapper()
    override val url by lazy { "${allNet.gameUrl.ensureEndingSlash()}Maimai2Servlet" }

    class UserMusicWrapper(var userMusicDetailList: List<Mai2UserMusicDetail>)

    override fun pull(): String {
        val (userId, paged) = prePull()

        return Mai2UserAll().apply {
            userData = ls("GetUserDataApi".get("userData", userId))
            userOption = ls("GetUserOptionApi".get("userOption", userId))
            userExtend = ls("GetUserExtendApi".get("userExtend", userId))
            userRatingList = ls("GetUserRatingApi".get("userRating", userId))
            userActivityList = ls("GetUserActivityApi".get("userActivity", userId))

            userMusicDetailList = "GetUserMusicApi".get<List<UserMusicWrapper>>("userMusicList", paged)
                .flatMap { it.userMusicDetailList }
            userFriendSeasonRankingList = "GetUserFriendSeasonRankingApi".get("userFriendSeasonRankingList", paged)
            userCharacterList = "GetUserCharacterApi".get("userCharacterList", paged)
            userItemList = (1..12).flatMap {
                "GetUserItemApi".get<List<Mai2UserItem>>("userItemList", paged + mapOf("nextIndex" to 10000000000 * it))
            }
            userCourseList = "GetUserCourseApi".get("userCourseList", paged)
            userFavoriteList = (1..5).mapNotNull {
                "GetUserFavoriteApi".getNullable<Mai2UserFavorite>("userFavorite", userId + mapOf("itemKind" to it))
            }
            userGhost = "GetUserGhostApi".get("userGhostList", userId)
            userMapList = "GetUserMapApi".get("userMapList", paged)
            userLoginBonusList = "GetUserLoginBonusApi".get("userLoginBonusList", userId)

            // TODO: userFavoriteMusicList
        }.toJson()
    }
}

class OngekiDataBroker(allNet: AllNetClient, log: (String) -> Unit): DataBroker(allNet, log) {
    override val mapper = BasicMapper()
    override val url by lazy { allNet.gameUrl.ensureNoEndingSlash() }

    override fun pull(): String {
        val (userId, paged) = prePull()

        return UpsertUserAll().apply {
            userData = ls("GetUserDataApi".get("userData", userId))
            userOption = ls("GetUserOptionApi".get("userOption", userId))
            userMusicItemList = "GetUserMusicItemApi".get("userMusicItemList", paged)
            userBossList = "GetUserBossApi".get("userBossList", userId)
            userMusicDetailList = "GetUserMusicApi".get("userMusicList", paged)
            userTechCountList = "GetUserTechCountApi".get("userTechCountList", userId)
            userCardList = "GetUserCardApi".get("userCardList", paged)
            userCharacterList = "GetUserCharacterApi".get("userCharacterList", paged)
            userStoryList = "GetUserStoryApi".get("userStoryList", userId)
            userChapterList = "GetUserChapterApi".get("userChapterList", userId)
            userMemoryChapterList = "GetUserMemoryChapterApi".get("userMemoryChapterList", userId)
            userDeckList = "GetUserDeckByKeyApi".get("userDeckList", userId + mapOf("authKey" to ""))
            userTrainingRoomList = "GetUserTrainingRoomByKeyApi".get("userTrainingRoomList", userId + mapOf("authKey" to ""))
            userActivityList = "GetUserActivityApi".get("userActivityList", userId + mapOf("kind" to 1))
            userRatinglogList = "GetUserRatinglogApi".get("userRatinglogList", userId)
            userRecentRatingList = "GetUserRecentRatingApi".get("userRecentRatingList", userId)
            userItemList = ls(2, 3, 4, 8, 9, 11, 12, 13, 14, 15, 16, 17, 19, 20).flatMap {
                "GetUserItemApi".get<List<UserItem>>("userItemList", paged + mapOf("nextIndex" to 10000000000 * it))
            }
            userEventPointList = "GetUserEventPointApi".get("userEventPointList", userId)
            userMissionPointList = "GetUserMissionPointApi".get("userMissionPointList", userId)
            userLoginBonusList = "GetUserLoginBonusApi".get("userLoginBonusList", userId)
            userScenarioList = "GetUserScenarioApi".get("userScenarioList", userId)
            userTradeItemList = "GetUserTradeItemApi".get("userTradeItemList", userId + mapOf("startChapterId" to 0, "endChapterId" to 99999))
            userEventMusicList = "GetUserEventMusicApi".get("userEventMusicList", userId)
            userTechEventList = "GetUserTechEventRankingApi".get("userTechEventRankingList", userId)
            userKopList = "GetUserKopApi".get("userKopList", userId)
        }.toJson()
    }
}
