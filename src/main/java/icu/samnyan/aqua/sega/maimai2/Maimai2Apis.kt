@file:Suppress("UNCHECKED_CAST")

package icu.samnyan.aqua.sega.maimai2

import ext.*
import icu.samnyan.aqua.sega.general.model.CardStatus
import icu.samnyan.aqua.sega.maimai2.model.UserRivalMusic
import icu.samnyan.aqua.sega.maimai2.model.UserRivalMusicDetail
import icu.samnyan.aqua.sega.maimai2.model.userdata.Mai2UserKaleidx
import icu.samnyan.aqua.sega.maimai2.model.userdata.UserRegions
import java.time.LocalDate
import kotlin.random.Random

fun Maimai2ServletController.initApis() {
    val log = logger()

    "GetUserExtend" { mapOf(
        "userId" to uid,
        "userExtend" to (db.userExtend.findSingleByUser_Card_ExtId(uid) ?: (404 - "User not found"))
    ) }

    "GetUserData" { mapOf(
        "userId" to uid,
        "userData" to (db.userData.findByCardExtId(uid) ?: (404 - "User not found")),
        "banState" to 0
    ) }

    "GetUserLoginBonus".unpaged { db.userLoginBonus.findByUser_Card_ExtId(uid) }
    "GetUserMap".unpaged { db.userMap.findByUser_Card_ExtId(uid) }
    "GetUserCard".unpaged { db.userCard.findByUser_Card_ExtId(uid) }
    "GetUserCharge".unpaged { db.userCharge.findByUser_Card_ExtId(uid) }
    "GetUserFriendSeasonRanking".unpaged { db.userFriendSeasonRanking.findByUser_Card_ExtId(uid) }
    "GetUserCourse".unpaged { db.userCourse.findByUser_Card_ExtId(uid) }

    "GetUserMusic".unpaged {
        ls(mapOf("userMusicDetailList" to db.userMusicDetail.findByUser_Card_ExtId(uid)))
    }

    "GetUserFavorite" { mapOf(
        "userId" to uid,
        "userFavorite" to db.userFavorite.findByUser_Card_ExtIdAndItemKind(uid, data["itemKind"] as Int)
    ) }

    "GetUserActivity" {
        db.userAct.findByUser_Card_ExtId(uid).let { act -> mapOf(
            "userActivity" to mapOf(
                "playList" to act.filter { it.kind == 1 }.take(200),
                "musicList" to act.filter { it.kind == 2 }.take(200)
            )
        ) }
    }

    // Maimai only request for event type 1
    "GetGameEvent" static { mapOf("type" to 1, "gameEventList" to db.gameEvent.findAll()) }
    "GetGameCharge" static { db.gameCharge.findAll().let { mapOf("length" to it.size, "gameChargeList" to it) } }

    "GetUserOption" { mapOf(
        "userId" to uid,
        "userOption" to (db.userOption.findSingleByUser_Card_ExtId(uid) ?: (404 - "User not found"))
    ) }

    "CreateToken" static { """{"Bearer":"meow"}""" }

    "CMUpsertUserPrintlog" static { """{"returnCode":1,"orderId":"0","serialId":"FAKECARDIMAG12345678"}""" }

    "CMGetSellingCard" static { db.gameSellingCard.findAll().let {
        mapOf("length" to it.size, "sellingCardList" to it)
    } }

    "CMGetUserCharacter" { db.userCharacter.findByUser_Card_ExtId(uid).let {
        mapOf(
            "returnCode" to 1,
            "length" to it.size,
            "userCharacterList" to it
        )
    } }

    "CMGetUserPreview" { db.userData.findByCardExtId(uid)?.let {
        mapOf(
            "userId" to uid,
            "userName" to it.userName,
            "rating" to it.playerRating,
            "lastDataVersion" to it.lastDataVersion,
            "isLogin" to false,
            "isExistSellingCard" to false
        )
    } ?: (404 - "User not found") }

    "GetUserPreview" {
        val d = db.userData.findByCardExtId(uid) ?: (404 - "User not found")
        val option = db.userOption.findSingleByUser_Card_ExtId(uid)

        val res = mutableMapOf(
            "userId" to uid,
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

        if (d.card?.status == CardStatus.MIGRATED_TO_MINATO) {
            res["userName"] = "${res["userName"]}＠ＡｑｕａＤＸ"
        }

        res
    }

    "UserLogin" {
        val d = db.userData.findByCardExtId(uid)

        val res = mutableMapOf(
            "returnCode" to 1, "loginCount" to 1,
            "lastLoginDate" to "2020-01-01 00:00:00.0",
            "consecutiveLoginCount" to 0, "loginId" to 1,
            "Bearer" to "meow", "bearer" to "meow"
        )

        // Get regionId from request
        val region = data["regionId"] as? Int

        // Only save if it is a valid region and the user has played at least a song
        if (region != null && region > 0 && d != null) {
            val region = db.userRegions.findByUserAndRegionId(d, region)?.apply {
                playCount += 1
            } ?: UserRegions().apply {
                user = d
                regionId = region
            }
            db.userRegions.save(region)
            // d.card?.let { cardService.updateCardTimestamp(it, "mai2") } // TODO: why save regions on login?
        }

        res
    }

    "GetUserShopStock" {
        val shopItemIdList = data["shopItemIdList"] as List<*>

        mapOf(
            "userId" to uid,
            "userShopStockList" to shopItemIdList.map { mapOf(
                "shopItemId" to it,
                "tradeCount" to 0
            ) }
        )
    }

    "GetUserRivalData" {
        val rivalId = parsing { data["rivalId"]!!.long }

        // rivalId should store and fetch with the id column of table rather than card_ext_id
        // or user will be able to get others' ext_id by setting them as rival
        mapOf(
            "userId" to uid,
            "userRivalData" to mapOf(
                "rivalId" to rivalId,
                "rivalName" to (db.userData.findById(rivalId)()?.userName ?: "")
            )
        )
    }

    "GetUserRivalMusic" {
        val rivalId = parsing { data["rivalId"]!!.long }

        val lst = db.userMusicDetail.findByUserId(rivalId)
        val res = lst.associate { it.musicId to UserRivalMusic(it.musicId) }

        lst.forEach {
            res[it.musicId]!!.userRivalMusicDetailList.add(
                UserRivalMusicDetail(it.level, it.achievement, it.deluxscoreMax)
            )
        }

        mapOf("userId" to uid, "rivalId" to rivalId, "nextIndex" to 0, "userRivalMusicList" to res.values)
    }

    "GetUserRegion" {
        logger().info("Getting user regions for user $uid")
        db.userRegions.findByUser_Card_ExtId(uid)
            .map { mapOf("regionId" to it.regionId, "playCount" to it.playCount) }
        .let { mapOf("userId" to uid, "length" to it.size, "userRegionList" to it) }
    }

    "GetUserIntimate".unpaged {
        val u = db.userData.findByCardExtId(uid) ?: (404 - "User not found")
        db.userIntimate.findByUser(u)
    }

    // Empty List Handlers
    "GetUserGhost".unpaged { empty }
    "GetUserFriendBonus" { mapOf("userId" to uid, "returnCode" to 0, "getMiles" to 0) }
    "GetTransferFriend" { mapOf("userId" to uid, "transferFriendList" to empty) }
    "GetUserNewItem" { mapOf("userId" to uid, "itemKind" to 0, "itemId" to 0) }

    "GetUserCardPrintError" static { mapOf("length" to 0, "userPrintDetailList" to empty) }
    "GetUserFriendCheck" static { mapOf("returnCode" to 0) }
    "UserFriendRegist" static { mapOf("returnCode1" to 0, "returnCode2" to 0) }
    "GetGameNgMusicId" static { mapOf("length" to 0, "musicIdList" to empty, "ngMusicDataList" to empty) }
    "GetGameNationalData" static { mapOf("nextIndex" to 0, "nationalDataList" to empty) }
    "GetGameTournamentInfo" static { mapOf("length" to 0, "gameTournamentInfoList" to empty) }

    // <phaseId: start offset days>
    val phases = mapOf(1 to 1, 2 to 7, 3 to 14, 4 to 21)
    // Find the minimum phase id that started prior to today.
    fun findPhase(baseDate: LocalDate): Int {
        val today = jstNow().toLocalDate()
        return phases.entries.find { baseDate.plusDays(it.value.toLong()) > today }?.key ?: 5
    }

    // Kaleidoscope, added on 1.50
    // [{gateId, phaseId}]
    "GetGameKaleidxScope" { mapOf("gameKaleidxScopeList" to ls(
        mapOf("gateId" to 1, "phaseId" to 6),
        mapOf("gateId" to 2, "phaseId" to 6),
        mapOf("gateId" to 3, "phaseId" to 6),
        mapOf("gateId" to 4, "phaseId" to 6),
        mapOf("gateId" to 5, "phaseId" to 6),
        mapOf("gateId" to 6, "phaseId" to 6),
        mapOf("gateId" to 7, "phaseId" to 6),
        mapOf("gateId" to 8, "phaseId" to 6),
        mapOf("gateId" to 9, "phaseId" to 6),
        mapOf("gateId" to 10, "phaseId" to 13),
    )) }
    // Request: {userId}
    // Response: {userId, userKaleidxScopeList}
    "GetUserKaleidxScope".unpaged {
        val u = db.userData.findByCardExtId(uid) ?: (404 - "User not found")
        val lst = db.userKaleidx.findByUser(u)
            .mapApply { isKeyFound = true }.toMutableList()

        lst += (1..6).filter { i -> lst.none { it.gateId == i } }
            .map { Mai2UserKaleidx().apply { user = u; gateId = it } }

        lst
    }
    // Request: {userId, version, userData: [UserDetail], userPlaylogList: [UserPlaylog]}
    // Response: {userId, userItemList: [UserItem]}
    // Added on 1.50
    "GetUserNewItemList" { mapOf("userId" to uid, "userItemList" to empty) }

    // Added on 1.50 (export only)
    "UploadUserPlaylogList" {
        val lst = parsing { data["userPlaylogList"] as List<JDict> }
        if (lst.size > 40) (400 - "Too many playlogs")
        lst.forEach {
            uploadUserPlaylog.handle(mapOf("userId" to uid, "userPlaylog" to it))
        }
        """{"returnCode":1,"apiName":"com.sega.maimai2servlet.api.UploadUserPlaylogListApi"}"""
    }

    "GetGameSetting" static {
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

    "GetGameWeeklyData" static { mapOf(
        "gameWeeklyData" to mapOf(
            "missionCategory" to 0,
            "updateDate" to "2024-01-01 00:00:00.0",
            "beforeDate" to "2077-01-01 00:00:00.0"
        )
    ) }

    "GetUserMissionData" { mapOf(
        "userId" to uid,
        "userWeeklyData" to mapOf (
            "lastLoginWeek" to "",
            "beforeLoginWeek" to "",
            "friendBonusFlag" to false
        ),
        "userMissionDataList" to empty
    ) }

    // Added on 1.50
    // This unfortunately cannot be used for custom charts, because sound data is not present.
    "GetGameMusicScore" { mapOf(
        "gameMusicScore" to mapOf(
            "musicId" to data["musicId"],
            "level" to data["level"],
            "type" to data["type"],
            "scoreData" to ""
        )
    ) }

    // Recommendation
    "GetUserRecommendRateMusic" { mapOf(
        "userId" to uid,
        "userRecommendRateMusicIdList" to empty // TODO
    ) }

    "GetUserRecommendSelectMusic" {
        val user = db.userData.findByCard_ExtId(uid) ?: (404 - "User not found")
        mapOf(
            "userId" to uid,
            "userRecommendSelectionMusicIdList" to (net.recommendedMusic[user.id] ?: empty)
        )
    }

    "GetGameFesta" { mapOf(
        "eventId" to 0,
        "isRallyPeriod" to false,
        "isCircleJoinNotAllowed" to false,
        "jackingFestaSideId" to Random.nextInt(0, 3),
        "festaSideDataList" to empty,
    ) }

    "GetPlaceCircleData" static { mapOf(
        "returnCode" to 0,
        "circleId" to 0,
        "aggrDate" to ""
    ) }

    "GetUserCircleData" static { mapOf(
        "circleId" to 0,
        "circleName" to "一緒に歌おう！",
        "isPlace" to false,
        "circleClass" to 0,
        "lastLoginDate" to "",
        "circlePointRankingList" to empty
    ) }

    "GetUserCircleChallenge" { mapOf(
        "userId" to uid,
        "userCircleChallenge" to null,
        "circleCircleChallenge" to null,
        "achievement" to 0
    ) }

    "GetUserCirclePointData" { mapOf(
        "userId" to uid,
        "aggrDate" to "",
        "userCirclePointDataList" to empty
    ) }

    "GetUserCirclePointRanking" static { mapOf(
        "circleId" to 0,
        "circleName" to "一緒に歌おう！",
        "aggrDate" to "",
        "lastMonthCircleRank" to 0,
        "lastMonthPoint" to 0
    ) }

    "GetUserFesta" static { mapOf(
        "userFestaData" to mapOf(
            "eventId" to 0,
            "circleId" to 0,
            "festaSideId" to 0,
            "circleTotalFestaPoint" to 0,
            "currentTotalFestaPoint" to 0,
            "circleRankInFestaSide" to 0,
            "circleRecordDate" to "",
            "isDailyBonus" to false,
            "participationRewardGet" to false,
            "receivedRewardBorder" to 0
        ),
        "userResultFestaData" to mapOf(
            "eventId" to 0,
            "circleId" to 0,
            "circleName" to "一緒に歌おう！",
            "festaSideId" to 0,
            "circleRankInFestaSide" to 0,
            "receivedRewardBorder" to 0,
            "circleTotalFestaPoint" to 0,
            "resultRewardGet" to false,
        )
    ) }

    "UpsertUserPlaceCircleRegist" static { mapOf(
        "returnCode" to 0,
        "apiName" to "UpsertUserPlaceCircleRegistApi"
    ) }
}
