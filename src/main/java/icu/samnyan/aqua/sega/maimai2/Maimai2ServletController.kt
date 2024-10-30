package icu.samnyan.aqua.sega.maimai2

import ext.*
import icu.samnyan.aqua.net.utils.ApiException
import icu.samnyan.aqua.sega.general.BaseHandler
import icu.samnyan.aqua.sega.maimai2.handler.*
import icu.samnyan.aqua.sega.maimai2.model.Mai2Repos
import io.ktor.client.request.*
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.format.DateTimeFormatter
import kotlin.reflect.full.declaredMemberProperties

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Suppress("unused")
@RestController
@RequestMapping(path = ["/g/mai2/Maimai2Servlet/", "/g/mai2/"])
class Maimai2ServletController(
    val userLogin: UserLoginHandler,
    val upsertUserAll: UpsertUserAllHandler,
    val getUserItem: GetUserItemHandler,
    val getUserRating: GetUserRatingHandler,
    val uploadUserPhoto: UploadUserPhotoHandler,
    val uploadUserPlaylog: UploadUserPlaylogHandler,
    val getUserPortrait: GetUserPortraitHandler,
    val uploadUserPortrait: UploadUserPortraitHandler,
    val upsertUserPrint: UpsertUserPrintHandler,
    val getUserFavoriteItem: GetUserFavoriteItemHandler,
    val getUserRivalMusic: GetUserRivalMusicHandler,
    val getUserCharacter: GetUserCharacterHandler,
    val repos: Mai2Repos
) {
    companion object {
        private val logger = LoggerFactory.getLogger(Maimai2ServletController::class.java)
        private val empty = listOf<Any>()
        private val GAME_SETTING_DATE_FMT = DateTimeFormatter.ofPattern("2010-01-01 HH:mm:00.0")
        private val GAME_SETTING_TIME_FMT = DateTimeFormatter.ofPattern("HH:mm:00")
    }

    val getUserExtend = UserReqHandler { _, userId -> mapOf(
        "userId" to userId,
        "userExtend" to (repos.userExtend.findSingleByUser_Card_ExtId(userId)() ?: (404 - "User not found"))
    ) }

    val getUserData = UserReqHandler { _, userId -> mapOf(
        "userId" to userId,
        "userData" to (repos.userData.findByCardExtId(userId)() ?: (404 - "User not found")),
        "banState" to 0
    ) }

    val getUserLoginBonus = UserReqHandler { _, userId -> mapOf(
        "userId" to userId,
        "nextIndex" to 0,
        "userLoginBonusList" to repos.userLoginBonus.findByUser_Card_ExtId(userId)
    ) }

    val getUserMap = UserReqHandler { _, userId -> mapOf(
        "userId" to userId,
        "nextIndex" to 0,
        "userMapList" to repos.userMap.findByUser_Card_ExtId(userId)
    ) }

    val getUserMusic = UserReqHandler { _, userId -> mapOf(
        "userId" to userId,
        "nextIndex" to 0,
        "userMusicList" to listOf(mapOf("userMusicDetailList" to repos.userMusicDetail.findByUser_Card_ExtId(userId)))
    ) }

    val getUserCard = UserReqHandler { _, userId -> mapOf(
        "userId" to userId,
        "nextIndex" to 0,
        "userCardList" to repos.userCard.findByUser_Card_ExtId(userId)
    ) }

    val getUserCharge = UserReqHandler { _, userId -> repos.userCharge.findByUser_Card_ExtId(userId).let { mapOf(
        "userId" to userId,
        "length" to it.size,
        "userChargeList" to it
    ) } }

    val getUserFriendSeasonRanking = UserReqHandler { _, userId -> mapOf(
        "userId" to userId,
        "nextIndex" to 0,
        "userFriendSeasonRankingList" to repos.userFriendSeasonRanking.findByUser_Card_ExtId(userId)
    ) }

    val getUserCourse = UserReqHandler { _, userId -> mapOf(
        "userId" to userId,
        "nextIndex" to 0,
        "userCourseList" to repos.userCourse.findByUser_Card_ExtId(userId)
    ) }

    val getUserFavorite = UserReqHandler { req, userId -> mapOf(
        "userId" to userId,
        "userFavorite" to repos.userFavorite.findByUser_Card_ExtIdAndItemKind(userId, req["itemKind"] as Int)
    ) }

    val getUserActivity = UserReqHandler { _, userId ->
        repos.userAct.findByUser_Card_ExtId(userId).let { act -> mapOf(
            "userActivity" to mapOf(
                "playList" to act.filter { it.kind == 1 },
                "musicList" to act.filter { it.kind == 2 }
            )
        ) }
    }

    val getGameCharge = BaseHandler { repos.gameCharge.findAll().let {
        mapOf("length" to it.size, "gameChargeList" to it)
    } }

    val getGameEvent = BaseHandler {
        val type = parsing { (it["type"] as Number).toInt() }
        mapOf(
            "type" to type,
            "gameEventList" to repos.gameEvent.findByEnable(true) // Maimai only request for type 1
        )
    }

    val getUserRivalData = UserReqHandler { req, userId ->
        val rivalId = parsing { (req["rivalId"] as Number).toLong() }

        // rivalId should store and fetch with the id column of table rather than card_ext_id
        // or user will be able to get others' ext_id by setting them as rival
        mapOf(
            "userId" to userId,
            "userRivalData" to mapOf(
                "rivalId" to rivalId,
                "rivalName" to (repos.userData.findById(rivalId)()?.userName ?: "")
            )
        )
    }

    val getUserOption = UserReqHandler { _, userId -> mapOf(
        "userId" to userId,
        "userOption" to (repos.userOption.findSingleByUser_Card_ExtId(userId)() ?: (404 - "User not found"))
    ) }

    val cmGetSellingCard = BaseHandler { repos.gameSellingCard.findAll().let {
        mapOf("length" to it.size, "sellingCardList" to it)
    } }

    val cmGetUserCharacter = UserReqHandler { _, userId -> repos.userCharacter.findByUser_Card_ExtId(userId).let {
        mapOf(
            "returnCode" to 1,
            "length" to it.size,
            "userCharacterList" to it
        )
    } }

    val cmGetUserPreview = UserReqHandler { _, userId -> repos.userData.findByCardExtId(userId)()?.let {
        mapOf(
            "userId" to userId,
            "userName" to it.userName,
            "rating" to it.playerRating,
            "lastDataVersion" to it.lastDataVersion,
            "isLogin" to false,
            "isExistSellingCard" to false
        )
    } ?: (404 - "User not found") }

    val getUserPreview = UserReqHandler { _, userId ->
        val d = repos.userData.findByCardExtId(userId)() ?: (404 - "User not found")
        val option = repos.userOption.findSingleByUser_Card_ExtId(userId)()

        mapOf(
            "userId" to userId,
            "userName" to d.userName,
            "isLogin" to false,
            "lastGameId" to d.lastGameId,
            "lastDataVersion" to d.lastDataVersion,
            "lastRomVersion" to d.lastRomVersion,
            "lastLoginDate" to d.lastPlayDate,
            "lastPlayDate" to d.lastPlayDate,
            "playerRating" to d.playerRating,
            "nameplateId" to d.plateId,
            "iconId" to d.iconId,
            "trophyId" to 0,
            "partnerId" to d.partnerId,
            "frameId" to d.frameId,
            "totalAwake" to d.totalAwake,
            "isNetMember" to d.isNetMember,
            "dailyBonusDate" to d.dailyBonusDate,
            "headPhoneVolume" to (option?.headPhoneVolume ?: 0),
            "dispRate" to (option?.dispRate ?: 0),
            "isInherit" to false,
            "banState" to d.banState
        )
    }

    val getUserShopStock = UserReqHandler { req, userId ->
        val shopItemIdList = req["shopItemIdList"] as List<*>

        mapOf(
            "userId" to userId,
            "userShopStockList" to shopItemIdList.map { mapOf(
                "shopItemId" to it,
                "tradeCount" to 0
            ) }
        )
    }

    // Empty List Handlers
    val getUserRecommendRateMusic = UserReqHandler { _, userId -> mapOf(
        "userId" to userId,
        "userRecommendRateMusicIdList" to empty
    ) }
    val getUserRecommendSelectMusic = UserReqHandler { _, uid -> mapOf(
        "userId" to uid,
        "userRecommendSelectionMusicIdList" to empty
    ) }
    val getUserCardPrintError = BaseHandler { mapOf("length" to 0, "userPrintDetailList" to empty) }
    val getUserRegion = UserReqHandler { _, uid -> mapOf("userId" to uid, "length" to 0, "userRegionList" to empty) }
    val getUserGhost = UserReqHandler { _, uid -> mapOf("userId" to uid, "userGhostList" to empty) }
    val getUserFriendBonus = UserReqHandler { _, uid -> mapOf("userId" to uid, "returnCode" to 0, "getMiles" to 0) }
    val getUserFriendCheck = BaseHandler { mapOf("returnCode" to 0) }
    val userFriendRegist = BaseHandler { mapOf("returnCode1" to 0, "returnCode2" to 0) }
    val getUserIntimate = UserReqHandler { _, uid -> mapOf("userId" to uid, "length" to 0, "userIntimateList" to empty) }
    val getTransferFriend = UserReqHandler { _, uid -> mapOf("userId" to uid, "transferFriendList" to empty) }
    val getGameNgMusicId = BaseHandler { mapOf("length" to 0, "musicIdList" to empty) }
    val getGameRanking = BaseHandler { mapOf("type" to it["type"].toString(), "gameRankingList" to empty) }
    val getGameTournamentInfo = BaseHandler { mapOf("length" to 0, "gameTournamentInfoList" to empty) }
    val getGameKaleidxScope = BaseHandler { mapOf("gameKaleidxScopeList" to empty) }
    val getUserKaleidxScope = UserReqHandler { _, uid -> mapOf("userId" to uid, "userKaleidxScopeList" to empty) }
    val getUserNewItem = UserReqHandler { _, uid -> mapOf("userId" to uid, "itemKind" to 0, "itemId" to 0) }
    val getUserNewItemList = UserReqHandler { _, uid -> mapOf("userId" to uid, "userItemList" to empty) }

    val getGameSetting = BaseHandler {
        // The client-side implementation for reboot time is extremely cursed.
        // Only hour and minute are used, date is discarded and second is set to 0.
        // The time is adjusted to the next day if it's 12 hours or more from now.
        // And it's using local timezone instead of treating it as UTC.
        // The official maimai cabs will reboot every day, but we don't want that
        // So, we need to return the hour and minute 5 hours ago
        // val rebootStart = Instant.now().atZone(ZoneId.of("Asia/Tokyo")).minusHours(5)
        // val rebootEnd = rebootStart.plusSeconds(60)
        // Nope that didn't work

        mapOf(
            "isAouAccession" to true,
            "gameSetting" to mapOf(
//                "rebootStartTime" to GAME_SETTING_DATE_FMT.format(rebootStart),
//                "rebootEndTime" to GAME_SETTING_DATE_FMT.format(rebootEnd),
                "rebootStartTime" to "2020-01-01 23:59:00.0",
                "rebootEndTime" to "2020-01-01 23:59:00.0",
                "rebootInterval" to 0,

                // Fields below doesn't seem to be used by the client at all
                "isMaintenance" to false,
                "requestInterval" to 10,
                "movieUploadLimit" to 0,
                "movieStatus" to 0,
                "movieServerUri" to "",
                "deliverServerUri" to "",
                "oldServerUri" to "",
                "usbDlServerUri" to "",

                // Fields below are SDGB-specific settings (not present in SDEZ)
                "pingDisable" to true,
                "packetTimeout" to 20_000,
                "packetTimeoutLong" to 60_000,
                "packetRetryCount" to 5,
                "userDataDlErrTimeout" to 300_000,
                "userDataDlErrRetryCount" to 5,
                "userDataDlErrSamePacketRetryCount" to 5,
                "userDataUpSkipTimeout" to 0,
                "userDataUpSkipRetryCount" to 0,
                "iconPhotoDisable" to true,
                "uploadPhotoDisable" to false,
                "maxCountMusic" to 0,
                "maxCountItem" to 0
            )
        )
    }

    val getGameWeeklyData = BaseHandler { mapOf(
        "gameWeeklyData" to mapOf(
            "missionCategory" to 0,
            "updateDate" to "2024-01-01 00:00:00.0",
            "beforeDate" to "2077-01-01 00:00:00.0"
        )
    ) }

    val getUserMissionData = UserReqHandler { _, uid -> mapOf(
        "userId" to uid,
        "userWeeklyData" to mapOf (
            "lastLoginWeek" to "",
            "beforeLoginWeek" to "",
            "friendBonusFlag" to false
        ),
        "userMissionDataList" to empty
    ) }

    val getGameMusicScore = BaseHandler { mapOf(
        "gameMusicScore" to mapOf(
            "musicId" to 0,
            "level" to 0,
            "type" to 0,
            "scoreData" to ""
        )
    ) }

    val endpointList = setOf("GetGameEventApi", "GetGameRankingApi", "GetGameSettingApi", "GetGameTournamentInfoApi", "GetGameWeeklyDataApi",
        "GetTransferFriendApi", "GetUserActivityApi", "GetUserCardApi", "GetUserCharacterApi", "GetUserDataApi",
        "GetUserExtendApi", "GetUserFavoriteApi", "GetUserGhostApi", "GetUserItemApi", "GetUserLoginBonusApi",
        "GetUserMapApi", "GetUserMusicApi", "GetUserOptionApi", "GetUserPortraitApi", "GetUserPreviewApi",
        "GetUserFriendBonusApi", "GetUserFriendCheckApi", "UserFriendRegistApi", "GetUserMissionDataApi", "GetUserIntimateApi", "GetUserShopStockApi",
        "GetUserRatingApi", "GetUserRegionApi", "UploadUserPhotoApi", "UploadUserPlaylogApi", "UploadUserPortraitApi",
        "UserLoginApi", "UserLogoutApi", "UpsertUserAllApi", "GetGameChargeApi", "GetUserChargeApi",
        "GetUserCourseApi", "GetGameNgMusicIdApi", "GetUserFriendSeasonRankingApi", "CreateTokenApi",
        "GetUserRecommendRateMusicApi", "GetUserRecommendSelectMusicApi", "CMGetSellingCardApi",
        "CMGetUserCardApi", "CMGetUserCardPrintErrorApi", "CMGetUserCharacterApi", "CMGetUserDataApi", "CMGetUserItemApi",
        "CMGetUserPreviewApi", "CMUpsertUserPrintApi",
        "CMUpsertUserPrintlogApi", "GetUserFavoriteItemApi", "GetUserRivalDataApi", "GetUserRivalMusicApi",
        "GetUserScoreRankingApi", "UpsertClientBookkeepingApi", "UpsertClientSettingApi",
        "UpsertClientTestmodeApi", "UpsertClientUploadApi", "Ping", "RemoveTokenApi", "CMLoginApi", "CMLogoutApi",
        "CMUpsertBuyCardApi", "GetGameSettingApi", "GetGameKaleidxScopeApi", "GetGameMusicScoreApi",
        "GetUserKaleidxScopeApi", "GetUserNewItemApi", "GetUserNewItemListApi").toMutableList()

    val noopEndpoint = endpointList.popAll("GetUserScoreRankingApi", "UpsertClientBookkeepingApi",
        "UpsertClientSettingApi", "UpsertClientTestmodeApi", "UpsertClientUploadApi", "Ping", "RemoveTokenApi",
        "CMLoginApi", "CMLogoutApi", "CMUpsertBuyCardApi", "UserLogoutApi", "GetGameMapAreaConditionApi",
        "UpsertUserChargelogApi")

    val staticEndpoint = mapOf(
        "CreateTokenApi" to """{"Bearer":"meow"}""",
        "CMUpsertUserPrintlogApi" to """{"returnCode":1,"orderId":"0","serialId":"FAKECARDIMAG12345678"}""",
    ).also { endpointList.popAll(it.keys.toList()) }

    val members = this::class.declaredMemberProperties
    val handlers: Map<String, BaseHandler> = endpointList.associateWith { api ->
        val name = api.replace("Api", "").lowercase()
        (members.find { it.name.lowercase() == name } ?: members.find { it.name.lowercase() == name.replace("cm", "") })
            ?.let { it.call(this) as BaseHandler }
            ?: throw IllegalArgumentException("Mai2: No handler found for $api")
    }

    @API("/{api}")
    fun handle(@PathVariable api: String, @RequestBody request: Map<String, Any>): Any {
        try {
            logger.info("Mai2 < $api : ${request.toJson()}") // TODO: Optimize logging

            if (api in noopEndpoint) {
                logger.info("Mai2 > $api no-op")
                return """{"returnCode":1,"apiName":"com.sega.maimai2servlet.api.$api"}"""
            }

            if (api in staticEndpoint) {
                logger.info("Mai2 > $api static")
                return staticEndpoint[api]!!
            }

            return handlers[api]?.handle(request)?.let { if (it is String) it else it.toJson() }?.also {
                if (api !in setOf("GetUserItemApi", "GetGameEventApi"))
                    logger.info("Mai2 > $api : $it")
            } ?: {
                logger.warn("Mai2 > $api not found")
                """{"returnCode":1,"apiName":"com.sega.maimai2servlet.api.$api"}"""
            }
        } catch (e: ApiException) {
            // It's a bad practice to return 200 ok on error, but this is what maimai does so we have to follow
            return ResponseEntity.ok().body("""{"returnCode":0,"apiName":"com.sega.maimai2servlet.api.$api","message":"${e.message?.replace("\"", "\\\"")} - ${e.code}"}""")
        }
    }
}
