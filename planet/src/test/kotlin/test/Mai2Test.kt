package test

import ext.*
import icu.samnyan.aqua.sega.util.ZLib
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

class Mai2Test : StringSpec({
    var userId = 0L

    suspend fun post(url: String, body: String): Pair<HttpResponse, Map<String, Any?>> {
        val resp = HTTP.post("$HOST/gs/$CLIENT_ID/mai2/$url") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }

        assert(resp.status.isSuccess()) { "Failed to post to $url: ${resp.status} - ${resp.bodyAsText()}" }

        return Pair(resp, ZLib.decompress(resp.body<ByteArray>()).decodeToString().jsonMap())
    }

    beforeTest {
        if (userId != 0L) return@beforeTest
        userId = registerUser()
    }

    "GetGameSettingApi" {
        println("UserID: $userId")
        post("GetGameSettingApi", """{"placeId":291,"clientId":"$CLIENT_ID"}""").let { (_, result) ->
            result shouldBe """{"gameSetting":{"requestInterval":10,"rebootStartTime":"2099-01-01 23:59:00.0","rebootEndTime":"2099-01-01 23:59:00.0","movieUploadLimit":10000,"movieStatus":0,"movieServerUri":"","deliverServerUri":"","oldServerUri":"","usbDlServerUri":"","rebootInterval":0,"isMaintenance":false},"isAouAccession":true}""".jsonMap()
        }
    }

    "GetGameRankingApi" {
        post("GetGameRankingApi", """{"type":1}""").let { (_, result) ->
            result shouldBe """{"type":"1","gameRankingList":[]}""".jsonMap()
        }
    }

    "GetGameEventApi" {
        post("GetGameEventApi", """{"type":1,"isAllEvent":true}""").let { (_, result) ->
            result.keys shouldBe setOf("type", "gameEventList")
            ((result["gameEventList"] as List<*>).first() as Map<*, *>).keys shouldBe setOf("id", "type", "startDate", "endDate")
        }
    }

    "GetGameTournamentInfoApi" {
        post("GetGameTournamentInfoApi", """{}""").let { (_, result) ->
            result shouldBe """{"length":0,"gameTournamentInfoList":[]}""".jsonMap()
        }
    }

    "GetGameChargeApi" {
        post("GetGameChargeApi", """{"isAll":false}""").let { (_, result) ->
            result shouldBe """{"length":5,"gameChargeList":[{"orderId":1,"chargeId":2,"price":1,"startDate":"2019-01-01 00:00:00.000000","endDate":"2099-01-01 00:00:00.000000"},{"orderId":2,"chargeId":3,"price":2,"startDate":"2019-01-01 00:00:00.000000","endDate":"2099-01-01 00:00:00.000000"},{"orderId":3,"chargeId":4,"price":3,"startDate":"2019-01-01 00:00:00.000000","endDate":"2099-01-01 00:00:00.000000"},{"orderId":4,"chargeId":5,"price":4,"startDate":"2019-01-01 00:00:00.000000","endDate":"2099-01-01 00:00:00.000000"},{"orderId":5,"chargeId":6,"price":5,"startDate":"2019-01-01 00:00:00.000000","endDate":"2099-01-01 00:00:00.000000"}]}""".jsonMap()
        }
    }

    "GetGameNgMusicIdApi" {
        post("GetGameNgMusicIdApi", """{}""").let { (_, result) ->
            result shouldBe """{"length":0,"musicIdList":[]}""".jsonMap()
        }
    }

    "UpsertClientSettingApi" {
        post("UpsertClientSettingApi", """{"clientSetting":{"placeId":291,"clientId":"$CLIENT_ID","placeName":"","regionId":1,"regionName":"W","bordId":"ACAE01A9999","romVersion":100,"isDevelop":true,"isAou":true}}""").let { (_, result) ->
            result shouldBe """{"returnCode":1,"apiName":"com.sega.maimai2servlet.api.UpsertClientSettingApi"}""".jsonMap()
        }
    }

    "UpsertClientTestmodeApi" {
        post("UpsertClientTestmodeApi", """{"clientTestmode":{"placeId":291,"clientId":"$CLIENT_ID","trackSingle":1,"trackMulti":0,"trackEvent":0,"totalMachine":4,"satelliteId":5,"cameraPosition":0}}""").let { (_, result) ->
            result shouldBe """{"returnCode":1,"apiName":"com.sega.maimai2servlet.api.UpsertClientTestmodeApi"}""".jsonMap()
        }
    }

    "UserLoginApi" {
        post("UserLoginApi", """{"userId":$userId,"accessCode":"$ACCESS_CODE","regionId":1,"placeId":291,"clientId":"$CLIENT_ID","dateTime":1711485182}""").let { (_, result) ->
            result shouldBe """{"returnCode":1,"lastLoginDate":"2020-01-01 00:00:00.0","loginCount":1,"consecutiveLoginCount":0,"loginId":1,"Bearer":"meow","bearer":"meow"}""".jsonMap()
        }
    }

    "UploadUserPlaylogApi" {
        post("UploadUserPlaylogApi", """{"userId":$userId,"userPlaylog":{"userId":0,"orderId":0,"playlogId":1,"version":1041000,"placeId":291,"placeName":"","loginDate":1711485915,"playDate":"2024-03-26","userPlayDate":"2024-03-27 05:49:38.0","type":0,"musicId":11479,"level":2,"trackNo":1,"vsMode":0,"vsUserName":"","vsStatus":0,"vsUserRating":0,"vsUserAchievement":0,"vsUserGradeRank":0,"vsRank":0,"playerNum":1,"playedUserId1":0,"playedUserName1":"","playedMusicLevel1":0,"playedUserId2":0,"playedUserName2":"","playedMusicLevel2":0,"playedUserId3":0,"playedUserName3":"","playedMusicLevel3":0,"characterId1":101,"characterLevel1":1,"characterAwakening1":0,"characterId2":400101,"characterLevel2":1,"characterAwakening2":0,"characterId3":105,"characterLevel3":1,"characterAwakening3":0,"characterId4":104,"characterLevel4":1,"characterAwakening4":0,"characterId5":103,"characterLevel5":1,"characterAwakening5":0,"achievement":997456,"deluxscore":1725,"scoreRank":11,"maxCombo":674,"totalCombo":674,"maxSync":674,"totalSync":0,"tapCriticalPerfect":276,"tapPerfect":194,"tapGreat":29,"tapGood":2,"tapMiss":0,"holdCriticalPerfect":56,"holdPerfect":23,"holdGreat":2,"holdGood":1,"holdMiss":0,"slideCriticalPerfect":42,"slidePerfect":0,"slideGreat":0,"slideGood":0,"slideMiss":0,"touchCriticalPerfect":32,"touchPerfect":0,"touchGreat":0,"touchGood":0,"touchMiss":0,"breakCriticalPerfect":9,"breakPerfect":7,"breakGreat":1,"breakGood":0,"breakMiss":0,"isTap":true,"isHold":true,"isSlide":true,"isTouch":true,"isBreak":true,"isCriticalDisp":true,"isFastLateDisp":true,"fastCount":24,"lateCount":11,"isAchieveNewRecord":true,"isDeluxscoreNewRecord":true,"comboStatus":1,"syncStatus":0,"isClear":true,"beforeRating":0,"afterRating":271,"beforeGrade":0,"afterGrade":0,"afterGradeRank":0,"beforeDeluxRating":0,"afterDeluxRating":271,"isPlayTutorial":true,"isEventMode":false,"isFreedomMode":false,"playMode":0,"isNewFree":true,"trialPlayAchievement":-1,"extNum1":0,"extNum2":0,"extNum4":106,"extBool1":false}}""").let { (_, result) ->
            result shouldBe """{"returnCode":1,"apiName":"com.sega.maimai2servlet.api.UploadUserPlaylogApi"}""".jsonMap()
        }
    }

    "UpsertUserAllApi" {
        post("UpsertUserAllApi", """{"userId":$userId,"playlogId":1,"isEventMode":false,"isFreePlay":true,"upsertUserAll":{"userData":[{"accessCode":"$ACCESS_CODE","userName":"ＡＺＡ☆","isNetMember":0,"iconId":11,"plateId":1,"titleId":11,"partnerId":1,"frameId":1,"selectMapId":400001,"totalAwake":0,"gradeRating":0,"musicRating":271,"playerRating":271,"highestRating":271,"gradeRank":0,"classRank":0,"courseRank":0,"charaSlot":[101,400101,105,104,103],"charaLockSlot":[0,0,0,0,0],"contentBit":2169888,"playCount":1,"currentPlayCount":1,"renameCredit":0,"mapStock":0,"eventWatchedDate":"2024-03-27 05:45:15.0","lastGameId":"SDEZ","lastRomVersion":"1.41.00","lastDataVersion":"1.40.08","lastLoginDate":"2024-03-27 05:45:15.0","lastPlayDate":"2024-03-27 05:56:54.0","lastPlayCredit":1,"lastPlayMode":0,"lastPlaceId":291,"lastPlaceName":"","lastAllNetId":0,"lastRegionId":1,"lastRegionName":"W","lastClientId":"$CLIENT_ID","lastCountryCode":"JPN","lastSelectEMoney":0,"lastSelectTicket":0,"lastSelectCourse":0,"lastCountCourse":0,"firstGameId":"SDEZ","firstRomVersion":"1.41.00","firstDataVersion":"1.40.08","firstPlayDate":"2024-03-27 05:45:15.0","compatibleCmVersion":"1.40.00","dailyBonusDate":"1970-01-01 09:00:00.0","dailyCourseBonusDate":"1970-01-01 09:00:00.0","lastPairLoginDate":"1970-01-01 09:00:00.0","lastTrialPlayDate":"1970-01-01 09:00:00.0","playVsCount":0,"playSyncCount":0,"winCount":0,"helpCount":0,"comboCount":0,"totalDeluxscore":1725,"totalBasicDeluxscore":0,"totalAdvancedDeluxscore":0,"totalExpertDeluxscore":1725,"totalMasterDeluxscore":0,"totalReMasterDeluxscore":0,"totalSync":0,"totalBasicSync":0,"totalAdvancedSync":0,"totalExpertSync":0,"totalMasterSync":0,"totalReMasterSync":0,"totalAchievement":997456,"totalBasicAchievement":0,"totalAdvancedAchievement":0,"totalExpertAchievement":997456,"totalMasterAchievement":0,"totalReMasterAchievement":0,"playerOldRating":271,"playerNewRating":0,"banState":0,"dateTime":1711485182}],"userExtend":[{"selectMusicId":11479,"selectDifficultyId":2,"categoryIndex":106,"musicIndex":114,"extraFlag":529,"selectScoreType":1,"extendContentBit":1,"isPhotoAgree":false,"isGotoCodeRead":false,"selectResultDetails":false,"selectResultScoreViewType":0,"sortCategorySetting":0,"sortMusicSetting":0,"playStatusSetting":0,"selectedCardList":[],"encountMapNpcList":[]}],"userOption":[{"optionKind":3,"noteSpeed":24,"slideSpeed":10,"touchSpeed":21,"tapDesign":0,"holdDesign":0,"slideDesign":0,"starType":0,"outlineDesign":15,"noteSize":1,"slideSize":1,"touchSize":1,"starRotate":1,"dispCenter":1,"outFrameType":2,"dispChain":2,"dispRate":0,"dispBar":0,"touchEffect":0,"submonitorAnimation":2,"submonitorAchive":0,"submonitorAppeal":0,"matching":1,"trackSkip":0,"brightness":0,"mirrorMode":0,"dispJudge":8,"dispJudgePos":5,"dispJudgeTouchPos":1,"adjustTiming":34,"judgeTiming":34,"ansVolume":6,"tapHoldVolume":5,"criticalSe":0,"tapSe":0,"breakSe":0,"breakVolume":5,"exSe":0,"exVolume":5,"slideSe":0,"slideVolume":5,"breakSlideVolume":5,"touchVolume":5,"touchHoldVolume":5,"damageSeVolume":5,"headPhoneVolume":0,"sortTab":0,"sortMusic":0}],"userCharacterList":[{"characterId":101,"level":2,"awakening":0,"useCount":0},{"characterId":102,"level":1,"awakening":0,"useCount":0},{"characterId":103,"level":2,"awakening":0,"useCount":0},{"characterId":104,"level":2,"awakening":0,"useCount":0},{"characterId":105,"level":2,"awakening":0,"useCount":0},{"characterId":201,"level":1,"awakening":0,"useCount":0},{"characterId":202,"level":1,"awakening":0,"useCount":0},{"characterId":203,"level":1,"awakening":0,"useCount":0},{"characterId":204,"level":1,"awakening":0,"useCount":0},{"characterId":205,"level":1,"awakening":0,"useCount":0},{"characterId":301,"level":1,"awakening":0,"useCount":0},{"characterId":302,"level":1,"awakening":0,"useCount":0},{"characterId":303,"level":1,"awakening":0,"useCount":0},{"characterId":304,"level":1,"awakening":0,"useCount":0},{"characterId":305,"level":1,"awakening":0,"useCount":0},{"characterId":306,"level":1,"awakening":0,"useCount":0},{"characterId":392,"level":1,"awakening":0,"useCount":0},{"characterId":393,"level":1,"awakening":0,"useCount":0},{"characterId":394,"level":1,"awakening":0,"useCount":0},{"characterId":395,"level":1,"awakening":0,"useCount":0},{"characterId":401,"level":1,"awakening":0,"useCount":0},{"characterId":402,"level":1,"awakening":0,"useCount":0},{"characterId":403,"level":1,"awakening":0,"useCount":0},{"characterId":404,"level":1,"awakening":0,"useCount":0},{"characterId":405,"level":1,"awakening":0,"useCount":0},{"characterId":501,"level":1,"awakening":0,"useCount":0},{"characterId":502,"level":1,"awakening":0,"useCount":0},{"characterId":503,"level":1,"awakening":0,"useCount":0},{"characterId":504,"level":1,"awakening":0,"useCount":0},{"characterId":505,"level":1,"awakening":0,"useCount":0},{"characterId":601,"level":1,"awakening":0,"useCount":0},{"characterId":602,"level":1,"awakening":0,"useCount":0},{"characterId":603,"level":1,"awakening":0,"useCount":0},{"characterId":604,"level":1,"awakening":0,"useCount":0},{"characterId":605,"level":1,"awakening":0,"useCount":0},{"characterId":400101,"level":2,"awakening":0,"useCount":0}],"userGhost":[],"userMapList":[{"mapId":1,"distance":0,"isLock":false,"isClear":false,"isComplete":false},{"mapId":2,"distance":0,"isLock":false,"isClear":false,"isComplete":false},{"mapId":3,"distance":0,"isLock":false,"isClear":false,"isComplete":false},{"mapId":4,"distance":0,"isLock":false,"isClear":false,"isComplete":false},{"mapId":5,"distance":0,"isLock":false,"isClear":false,"isComplete":false},{"mapId":6,"distance":0,"isLock":false,"isClear":false,"isComplete":false},{"mapId":150001,"distance":0,"isLock":false,"isClear":false,"isComplete":false},{"mapId":150002,"distance":0,"isLock":false,"isClear":false,"isComplete":false},{"mapId":150003,"distance":0,"isLock":false,"isClear":false,"isComplete":false},{"mapId":150004,"distance":0,"isLock":false,"isClear":false,"isComplete":false},{"mapId":150005,"distance":0,"isLock":false,"isClear":false,"isComplete":false},{"mapId":350001,"distance":0,"isLock":false,"isClear":false,"isComplete":false},{"mapId":350003,"distance":0,"isLock":false,"isClear":false,"isComplete":false},{"mapId":350004,"distance":0,"isLock":false,"isClear":false,"isComplete":false},{"mapId":350006,"distance":0,"isLock":false,"isClear":false,"isComplete":false},{"mapId":350007,"distance":0,"isLock":false,"isClear":false,"isComplete":false},{"mapId":400001,"distance":14000,"isLock":false,"isClear":false,"isComplete":false},{"mapId":400002,"distance":0,"isLock":false,"isClear":false,"isComplete":false},{"mapId":400003,"distance":0,"isLock":false,"isClear":false,"isComplete":false},{"mapId":400004,"distance":0,"isLock":false,"isClear":false,"isComplete":false},{"mapId":400005,"distance":0,"isLock":false,"isClear":false,"isComplete":false},{"mapId":400006,"distance":0,"isLock":false,"isClear":false,"isComplete":false}],"userLoginBonusList":[{"bonusId":38,"point":1,"isCurrent":true,"isComplete":false}],"userRatingList":[{"rating":0,"ratingList":[{"musicId":11479,"level":2,"romVersion":23011,"achievement":997456}],"newRatingList":[],"nextRatingList":[],"nextNewRatingList":[],"udemae":{"rate":0,"maxRate":0,"classValue":0,"maxClassValue":0,"totalWinNum":0,"totalLoseNum":0,"maxWinNum":0,"maxLoseNum":0,"winNum":0,"loseNum":0,"npcTotalWinNum":0,"npcTotalLoseNum":0,"npcMaxWinNum":0,"npcMaxLoseNum":0,"npcWinNum":0,"npcLoseNum":0}}],"userItemList":[{"itemKind":1,"itemId":1,"stock":1,"isValid":true},{"itemKind":1,"itemId":11,"stock":1,"isValid":true},{"itemKind":2,"itemId":1,"stock":1,"isValid":true},{"itemKind":2,"itemId":11,"stock":1,"isValid":true},{"itemKind":2,"itemId":5023,"stock":1,"isValid":true},{"itemKind":3,"itemId":1,"stock":1,"isValid":true},{"itemKind":3,"itemId":10,"stock":1,"isValid":true},{"itemKind":3,"itemId":12,"stock":1,"isValid":true},{"itemKind":3,"itemId":11,"stock":1,"isValid":true},{"itemKind":10,"itemId":1,"stock":1,"isValid":true},{"itemKind":10,"itemId":17,"stock":1,"isValid":true},{"itemKind":11,"itemId":1,"stock":1,"isValid":true},{"itemKind":6,"itemId":11479,"stock":1,"isValid":true}],"userMusicDetailList":[{"musicId":11479,"level":2,"playCount":1,"achievement":997456,"comboStatus":1,"syncStatus":0,"deluxscoreMax":1725,"scoreRank":11,"extNum1":0}],"userCourseList":[],"userFriendSeasonRankingList":[],"userChargeList":[],"userFavoriteList":[],"userActivityList":[{"playList":[{"kind":1,"id":10,"sortNumber":1711485937,"param1":0,"param2":0,"param3":0,"param4":0},{"kind":1,"id":90,"sortNumber":1711486030,"param1":400005,"param2":0,"param3":0,"param4":0},{"kind":1,"id":30,"sortNumber":1711486333,"param1":11479,"param2":2,"param3":129,"param4":0},{"kind":1,"id":23,"sortNumber":1711486333,"param1":11479,"param2":2,"param3":129,"param4":0}],"musicList":[{"kind":2,"id":11479,"sortNumber":1711486333,"param1":0,"param2":0,"param3":0,"param4":0}]}],"userGamePlaylogList":[{"playlogId":1,"version":"1.41.00","playDate":"2024-03-27 05:56:54.0","playMode":0,"useTicketId":-1,"playCredit":1,"playTrack":1,"clientId":"$CLIENT_ID","isPlayTutorial":true,"isEventMode":false,"isNewFree":true,"playCount":189,"playSpecial":-1873749656,"playOtherUserId":0}],"user2pPlaylog":{"userId1":0,"userId2":0,"userName1":"","userName2":"","regionId":0,"placeId":0,"user2pPlaylogDetailList":[]},"isNewCharacterList":"111111111111111111111111111111111111","isNewMapList":"1111111111111111111111","isNewLoginBonusList":"1","isNewItemList":"1111111111111","isNewMusicDetailList":"1","isNewCourseList":"","isNewFavoriteList":"","isNewFriendSeasonRankingList":""}}""").let { (_, result) ->
            result shouldBe """{"returnCode":1,"apiName":"com.sega.maimai2servlet.api.UpsertUserAllApi"}""".jsonMap()
        }
    }

    "UserLogoutApi" {
        post("UserLogoutApi", """{"userId":$userId}""").let { (_, result) ->
            result shouldBe """{"returnCode":1,"apiName":"com.sega.maimai2servlet.api.UserLogoutApi"}""".jsonMap()
        }
    }

    "GetUserPreviewApi" {
        post("GetUserPreviewApi", """{"userId":$userId,"segaIdAuthKey":""}""").let { (_, result) ->
            result shouldBe """{"userId":$userId,"userName":"ＡＺＡ☆","lastGameId":"SDEZ","lastDataVersion":"1.40.08","lastRomVersion":"1.41.00","lastLoginDate":"2024-03-27 05:56:54.0","lastPlayDate":"2024-03-27 05:56:54.0","playerRating":271,"nameplateId":1,"iconId":11,"trophyId":0,"partnerId":1,"frameId":1,"dispRate":0,"totalAwake":0,"isNetMember":1,"dailyBonusDate":"1970-01-01 09:00:00.0","headPhoneVolume":0,"banState":0,"isLogin":false,"isInherit":false}""".jsonMap()
        }
    }

    "UserLoginApi2" {
        post("UserLoginApi", """{"userId":$userId,"accessCode":"$ACCESS_CODE","regionId":1,"placeId":291,"clientId":"$CLIENT_ID","dateTime":1711485182}""").let { (_, result) ->
            result shouldBe """{"returnCode":1,"lastLoginDate":"2020-01-01 00:00:00.0","loginCount":1,"consecutiveLoginCount":0,"loginId":1,"Bearer":"meow","bearer":"meow"}""".jsonMap()
        }    }

    "GetUserDataApi" {
        post("GetUserDataApi", """{"userId":$userId}""").let { (_, result) ->
            result shouldBe """{"userId":$userId,"userData":{"userName":"ＡＺＡ☆","friendCode":"","isNetMember":1,"nameplateId":0,"iconId":11,"trophyId":0,"plateId":1,"titleId":11,"partnerId":1,"frameId":1,"selectMapId":400001,"totalAwake":0,"gradeRating":0,"musicRating":271,"playerRating":271,"highestRating":271,"gradeRank":0,"classRank":0,"courseRank":0,"charaSlot":[101,400101,105,104,103],"charaLockSlot":[0,0,0,0,0],"contentBit":2169888,"playCount":1,"eventWatchedDate":"2024-03-27 05:45:15.0","lastGameId":"SDEZ","lastRomVersion":"1.41.00","lastDataVersion":"1.40.08","lastLoginDate":"2024-03-27 05:45:15.0","lastPlayDate":"2024-03-27 05:56:54.0","lastPlayCredit":1,"lastPlayMode":0,"lastPlaceId":291,"lastPlaceName":"","lastAllNetId":0,"lastRegionId":1,"lastRegionName":"W","lastClientId":"$CLIENT_ID","lastCountryCode":"JPN","lastSelectEMoney":0,"lastSelectTicket":0,"lastSelectCourse":0,"lastCountCourse":0,"firstGameId":"SDEZ","firstRomVersion":"1.41.00","firstDataVersion":"1.40.08","firstPlayDate":"2024-03-27 05:45:15.0","compatibleCmVersion":"1.40.00","dailyBonusDate":"1970-01-01 09:00:00.0","dailyCourseBonusDate":"1970-01-01 09:00:00.0","lastPairLoginDate":"1970-01-01 09:00:00.0","lastTrialPlayDate":"1970-01-01 09:00:00.0","playVsCount":0,"playSyncCount":0,"winCount":0,"helpCount":0,"comboCount":0,"totalDeluxscore":1725,"totalBasicDeluxscore":0,"totalAdvancedDeluxscore":0,"totalExpertDeluxscore":1725,"totalMasterDeluxscore":0,"totalReMasterDeluxscore":0,"totalHiscore":0,"totalBasicHighscore":0,"totalAdvancedHighscore":0,"totalExpertHighscore":0,"totalMasterHighscore":0,"totalReMasterHighscore":0,"totalSync":0,"totalBasicSync":0,"totalAdvancedSync":0,"totalExpertSync":0,"totalMasterSync":0,"totalReMasterSync":0,"totalAchievement":997456,"totalBasicAchievement":0,"totalAdvancedAchievement":0,"totalExpertAchievement":997456,"totalMasterAchievement":0,"totalReMasterAchievement":0,"playerOldRating":271,"playerNewRating":0,"banState":0,"dateTime":1711485182,"cmLastEmoneyBrand":2,"cmLastEmoneyCredit":69,"mapStock":0,"currentPlayCount":1,"renameCredit":0,"accessCode":"$ACCESS_CODE"},"banState":0}""".jsonMap()
        }
    }

    "GetUserCardApi" {
        post("GetUserCardApi", """{"userId":$userId,"nextIndex":0,"maxCount":20}""").let { (_, result) ->
            result shouldBe """{"userId":$userId,"nextIndex":0,"userCardList":[]}""".jsonMap()
        }
    }

    "GetUserCharacterApi" {
        post("GetUserCharacterApi", """{"userId":$userId}""").let { (_, result) ->
            result.keys shouldBe setOf("userId", "userCharacterList")
            result["userId"] shouldBe userId
            ((result["userCharacterList"] as List<*>).first() as Map<*, *>).keys shouldBe setOf(
                "characterId", "point", "count", "level", "nextAwake", "nextAwakePercent", "favorite", "awakening", "useCount"
            )
        }
    }

    "GetUserItemApi" {
        infix fun Map<String, Any?>.userItemEquals(exp: Map<String, Any?>) {
            this["userId"] shouldBe userId
            this["nextIndex"] shouldBe exp["nextIndex"]
            this["itemKind"] shouldBe exp["itemKind"]
            (this["userItemList"] as List<*>).toSet() shouldBe (exp["userItemList"] as List<*>).toSet()
        }

        post("GetUserItemApi", """{"userId":$userId,"nextIndex":10000000000,"maxCount":100}""").let { (_, result) ->
            result userItemEquals """{"userId":$userId,"nextIndex":0,"itemKind":1,"userItemList":[{"itemKind":1,"itemId":1,"stock":1,"isValid":true},{"itemKind":1,"itemId":11,"stock":1,"isValid":true}]}""".jsonMap()
        }

        post("GetUserItemApi", """{"userId":$userId,"nextIndex":20000000000,"maxCount":100}""").let { (_, result) ->
            result userItemEquals """{"userId":$userId,"nextIndex":0,"itemKind":2,"userItemList":[{"itemKind":2,"itemId":1,"stock":1,"isValid":true},{"itemKind":2,"itemId":11,"stock":1,"isValid":true},{"itemKind":2,"itemId":5023,"stock":1,"isValid":true}]}""".jsonMap()
        }

        post("GetUserItemApi", """{"userId":$userId,"nextIndex":100000000000,"maxCount":100}""").let { (_, result) ->
            result userItemEquals """{"userId":$userId,"nextIndex":0,"itemKind":10,"userItemList":[{"itemKind":10,"itemId":1,"stock":1,"isValid":true},{"itemKind":10,"itemId":17,"stock":1,"isValid":true}]}""".jsonMap()
        }

        post("GetUserItemApi", """{"userId":$userId,"nextIndex":30000000000,"maxCount":100}""").let { (_, result) ->
            result userItemEquals """{"userId":$userId,"nextIndex":0,"itemKind":3,"userItemList":[{"itemKind":3,"itemId":1,"stock":1,"isValid":true},{"itemKind":3,"itemId":10,"stock":1,"isValid":true},{"itemKind":3,"itemId":11,"stock":1,"isValid":true},{"itemKind":3,"itemId":12,"stock":1,"isValid":true}]}""".jsonMap()
        }

        post("GetUserItemApi", """{"userId":$userId,"nextIndex":40000000000,"maxCount":100}""").let { (_, result) ->
            result userItemEquals """{"userId":$userId,"nextIndex":0,"itemKind":4,"userItemList":[]}""".jsonMap()
        }

        post("GetUserItemApi", """{"userId":$userId,"nextIndex":110000000000,"maxCount":100}""").let { (_, result) ->
            result userItemEquals """{"userId":$userId,"nextIndex":0,"itemKind":11,"userItemList":[{"itemKind":11,"itemId":1,"stock":1,"isValid":true}]}""".jsonMap()
        }

        post("GetUserItemApi", """{"userId":$userId,"nextIndex":120000000000,"maxCount":100}""").let { (_, result) ->
            result userItemEquals """{"userId":$userId,"nextIndex":0,"itemKind":12,"userItemList":[]}""".jsonMap()
        }


        post("GetUserItemApi", """{"userId":$userId,"nextIndex":50000000000,"maxCount":100}""").let { (_, result) ->
            result userItemEquals """{"userId":$userId,"nextIndex":0,"itemKind":5,"userItemList":[]}""".jsonMap()
        }

        post("GetUserItemApi", """{"userId":$userId,"nextIndex":60000000000,"maxCount":100}""").let { (_, result) ->
            result userItemEquals """{"userId":$userId,"nextIndex":0,"itemKind":6,"userItemList":[{"itemKind":6,"itemId":11479,"stock":1,"isValid":true}]}""".jsonMap()
        }

        post("GetUserItemApi", """{"userId":$userId,"nextIndex":70000000000,"maxCount":100}""").let { (_, result) ->
            result userItemEquals """{"userId":$userId,"nextIndex":0,"itemKind":7,"userItemList":[]}""".jsonMap()
        }

        post("GetUserItemApi", """{"userId":$userId,"nextIndex":80000000000,"maxCount":100}""").let { (_, result) ->
            result userItemEquals """{"userId":$userId,"nextIndex":0,"itemKind":8,"userItemList":[]}""".jsonMap()
        }
    }

    "GetUserCourseApi" {
        post("GetUserCourseApi", """{"userId":$userId,"nextIndex":0}""").let { (_, result) ->
            result shouldBe """{"userId":$userId,"nextIndex":0,"userCourseList":[]}""".jsonMap()
        }
    }

    "GetUserChargeApi" {
        post("GetUserChargeApi", """{"userId":$userId}""").let { (_, result) ->
            result shouldBe """{"userId":$userId,"length":0,"userChargeList":[]}""".jsonMap()
        }
    }

    "GetUserFavoriteApi" {
        post("GetUserFavoriteApi", """{"userId":$userId,"itemKind":1}""").let { (_, result) ->
            result shouldBe """{"userId":$userId,"userFavoriteData":[]}""".jsonMap()
        }


        post("GetUserFavoriteApi", """{"userId":$userId,"itemKind":2}""").let { (_, result) ->
            result shouldBe """{"userId":$userId,"userFavoriteData":[]}""".jsonMap()
        }


        post("GetUserFavoriteApi", """{"userId":$userId,"itemKind":3}""").let { (_, result) ->
            result shouldBe """{"userId":$userId,"userFavoriteData":[]}""".jsonMap()
        }


        post("GetUserFavoriteApi", """{"userId":$userId,"itemKind":4}""").let { (_, result) ->
            result shouldBe """{"userId":$userId,"userFavoriteData":[]}""".jsonMap()
        }


        post("GetUserFavoriteApi", """{"userId":$userId,"itemKind":5}""").let { (_, result) ->
            result shouldBe """{"userId":$userId,"userFavoriteData":[]}""".jsonMap()
        }
    }

    "GetUserGhostApi" {
        post("GetUserGhostApi", """{"userId":$userId}""").let { (_, result) ->
            result shouldBe """{"userId":$userId,"userGhostList":[]}""".jsonMap()
        }
    }

    "GetUserMapApi" {
        post("GetUserMapApi", """{"userId":$userId,"nextIndex":0,"maxCount":20}""").let { (_, result) ->
            result shouldBe """{"userId":$userId,"nextIndex":0,"userMapList":[{"mapId":1,"distance":0,"isLock":false,"isClear":false,"isComplete":false},{"mapId":2,"distance":0,"isLock":false,"isClear":false,"isComplete":false},{"mapId":3,"distance":0,"isLock":false,"isClear":false,"isComplete":false},{"mapId":4,"distance":0,"isLock":false,"isClear":false,"isComplete":false},{"mapId":5,"distance":0,"isLock":false,"isClear":false,"isComplete":false},{"mapId":6,"distance":0,"isLock":false,"isClear":false,"isComplete":false},{"mapId":150001,"distance":0,"isLock":false,"isClear":false,"isComplete":false},{"mapId":150002,"distance":0,"isLock":false,"isClear":false,"isComplete":false},{"mapId":150003,"distance":0,"isLock":false,"isClear":false,"isComplete":false},{"mapId":150004,"distance":0,"isLock":false,"isClear":false,"isComplete":false},{"mapId":150005,"distance":0,"isLock":false,"isClear":false,"isComplete":false},{"mapId":350001,"distance":0,"isLock":false,"isClear":false,"isComplete":false},{"mapId":350003,"distance":0,"isLock":false,"isClear":false,"isComplete":false},{"mapId":350004,"distance":0,"isLock":false,"isClear":false,"isComplete":false},{"mapId":350006,"distance":0,"isLock":false,"isClear":false,"isComplete":false},{"mapId":350007,"distance":0,"isLock":false,"isClear":false,"isComplete":false},{"mapId":400001,"distance":14000,"isLock":false,"isClear":false,"isComplete":false},{"mapId":400002,"distance":0,"isLock":false,"isClear":false,"isComplete":false},{"mapId":400003,"distance":0,"isLock":false,"isClear":false,"isComplete":false},{"mapId":400004,"distance":0,"isLock":false,"isClear":false,"isComplete":false},{"mapId":400005,"distance":0,"isLock":false,"isClear":false,"isComplete":false},{"mapId":400006,"distance":0,"isLock":false,"isClear":false,"isComplete":false}]}""".jsonMap()
        }
    }

    "GetUserLoginBonusApi" {
        post("GetUserLoginBonusApi", """{"userId":$userId,"nextIndex":0,"maxCount":20}""").let { (_, result) ->
            result shouldBe """{"userId":$userId,"nextIndex":0,"userLoginBonusList":[{"bonusId":38,"point":1,"isCurrent":true,"isComplete":false}]}""".jsonMap()
        }
    }

    "GetUserRegionApi" {
        post("GetUserRegionApi", """{"userId":$userId}""").let { (_, result) ->
            result shouldBe """{"userId":$userId,"length":0,"userRegionList":[]}""".jsonMap()
        }
    }

    "GetUserRecommendRateMusicApi" {
        post("GetUserRecommendRateMusicApi", """{"userId":$userId}""").let { (_, result) ->
            result shouldBe """{"userId":$userId,"userRecommendRateMusicIdList":[]}""".jsonMap()
        }
    }

    "GetUserRecommendSelectMusicApi" {
        post("GetUserRecommendSelectMusicApi", """{"userId":$userId}""").let { (_, result) ->
            result shouldBe """{"userId":$userId,"userRecommendSelectionMusicIdList":[]}""".jsonMap()
        }
    }

    "GetUserOptionApi" {
        post("GetUserOptionApi", """{"userId":$userId}""").let { (_, result) ->
            result shouldBe """{"userId":$userId,"userOption":{"optionKind":3,"noteSpeed":24,"slideSpeed":10,"touchSpeed":21,"tapDesign":0,"holdDesign":0,"slideDesign":0,"starType":0,"outlineDesign":15,"noteSize":1,"slideSize":1,"touchSize":1,"starRotate":1,"dispCenter":1,"dispChain":2,"dispRate":0,"dispBar":0,"touchEffect":0,"submonitorAnimation":2,"submonitorAchive":0,"submonitorAppeal":0,"matching":1,"trackSkip":0,"brightness":0,"mirrorMode":0,"dispJudge":8,"dispJudgePos":5,"dispJudgeTouchPos":1,"adjustTiming":34,"judgeTiming":34,"ansVolume":6,"tapHoldVolume":5,"criticalSe":0,"tapSe":0,"breakSe":0,"breakVolume":5,"exSe":0,"exVolume":5,"slideSe":0,"slideVolume":5,"touchHoldVolume":5,"damageSeVolume":5,"headPhoneVolume":0,"sortTab":0,"sortMusic":0,"outFrameType":2,"breakSlideVolume":5,"touchVolume":5}}""".jsonMap()
        }
    }

    "GetUserExtendApi" {
        post("GetUserExtendApi", """{"userId":$userId}""").let { (_, result) ->
            result shouldBe """{"userId":$userId,"userExtend":{"selectMusicId":11479,"selectDifficultyId":2,"categoryIndex":106,"musicIndex":114,"extraFlag":529,"selectScoreType":1,"extendContentBit":1,"isPhotoAgree":false,"isGotoCodeRead":false,"selectResultDetails":false,"sortCategorySetting":0,"sortMusicSetting":0,"playStatusSetting":0,"selectedCardList":[],"encountMapNpcList":[],"selectResultScoreViewType":0}}""".jsonMap()
        }
    }

    "GetUserRatingApi" {
        post("GetUserRatingApi", """{"userId":$userId}""").let { (_, result) ->
            result shouldBe """{"userId":$userId,"userRating":{"rating":271,"ratingList":[{"musicId":11479,"level":2,"romVersion":23011,"achievement":997456}],"newRatingList":[],"nextRatingList":[],"nextNewRatingList":[],"udemae":{"rate":0,"maxRate":0,"classValue":0,"maxClassValue":0,"totalWinNum":0,"totalLoseNum":0,"maxWinNum":0,"maxLoseNum":0,"winNum":0,"loseNum":0,"npcTotalWinNum":0,"npcTotalLoseNum":0,"npcMaxWinNum":0,"npcMaxLoseNum":0,"npcWinNum":0,"npcLoseNum":0}}}""".jsonMap()
        }
    }

    "GetUserMusicApi" {
        post("GetUserMusicApi", """{"userId":$userId,"nextIndex":0,"maxCount":50}""").let { (_, result) ->
            result shouldBe """{"userId":$userId,"nextIndex":0,"userMusicList":[{"userMusicDetailList":[{"musicId":11479,"level":2,"playCount":1,"achievement":997456,"comboStatus":1,"syncStatus":0,"deluxscoreMax":1725,"scoreRank":11,"extNum1":0}]}]}""".jsonMap()
        }
    }

    "GetUserPortraitApi" {
        post("GetUserPortraitApi", """{"userId":$userId}""").let { (_, result) ->
            result shouldBe """{"length":0,"userPortraitList":[]}""".jsonMap()
        }
    }

    "GetUserActivityApi" {
        post("GetUserActivityApi", """{"userId":$userId}""").let { (_, result) ->
            result.keys shouldBe setOf("userActivity")
            ((result["userActivity"] as Map<*, *>)["playList"] as List<*>).toSet() shouldBe setOf(
                mapOf("kind" to 1, "id" to 30, "sortNumber" to 1711486333, "param1" to 11479, "param2" to 2, "param3" to 129, "param4" to 0),
                mapOf("kind" to 1, "id" to 23, "sortNumber" to 1711486333, "param1" to 11479, "param2" to 2, "param3" to 129, "param4" to 0),
                mapOf("kind" to 1, "id" to 90, "sortNumber" to 1711486030, "param1" to 400005, "param2" to 0, "param3" to 0, "param4" to 0),
                mapOf("kind" to 1, "id" to 10, "sortNumber" to 1711485937, "param1" to 0, "param2" to 0, "param3" to 0, "param4" to 0)
            )
            ((result["userActivity"] as Map<*, *>)["musicList"] as List<*>).toSet() shouldBe setOf(
                mapOf("kind" to 2, "id" to 11479, "sortNumber" to 1711486333, "param1" to 0, "param2" to 0, "param3" to 0, "param4" to 0)
            )
        }
    }

    "GetUserFriendSeasonRankingApi" {
        post("GetUserFriendSeasonRankingApi", """{"userId":$userId,"nextIndex":0,"maxCount":20}""").let { (_, result) ->
            result shouldBe """{"userId":$userId,"nextIndex":0,"userFriendSeasonRankingList":[]}""".jsonMap()
        }
    }

    "GetUserFavoriteItemApi" {
        // TODO: Check if this api should return string or int
        post("GetUserFavoriteItemApi", """{"userId":$userId,"kind":2,"nextIndex":0,"maxCount":100,"isAllFavoriteItem":false}""").let { (_, result) ->
//            result shouldBe """{"userId":"$USER_ID","kind":"2","length":"0","nextIndex":"0","userFavoriteItemList":[]}""".jsonMap()
            result shouldBe """{"userId":$userId,"kind":2,"length":0,"nextIndex":0,"userFavoriteItemList":[]}""".jsonMap()
        }

        post("GetUserFavoriteItemApi", """{"userId":$userId,"kind":1,"nextIndex":0,"maxCount":100,"isAllFavoriteItem":false}""").let { (_, result) ->
//            result shouldBe """{"userId":"$USER_ID","kind":"1","length":"0","nextIndex":"0","userFavoriteItemList":[]}""".jsonMap()
            result shouldBe """{"userId":$userId,"kind":1,"length":0,"nextIndex":0,"userFavoriteItemList":[]}""".jsonMap()
        }
    }

    "UploadUserPlaylogApi2" {
        post("UploadUserPlaylogApi", """{"userId":$userId,"userPlaylog":{"userId":0,"orderId":0,"playlogId":1,"version":1041000,"placeId":291,"placeName":"","loginDate":1711486636,"playDate":"2024-03-26","userPlayDate":"2024-03-27 05:59:00.0","type":0,"musicId":11176,"level":2,"trackNo":1,"vsMode":0,"vsUserName":"","vsStatus":0,"vsUserRating":0,"vsUserAchievement":0,"vsUserGradeRank":0,"vsRank":0,"playerNum":1,"playedUserId1":0,"playedUserName1":"","playedMusicLevel1":0,"playedUserId2":0,"playedUserName2":"","playedMusicLevel2":0,"playedUserId3":0,"playedUserName3":"","playedMusicLevel3":0,"characterId1":101,"characterLevel1":2,"characterAwakening1":0,"characterId2":102,"characterLevel2":1,"characterAwakening2":0,"characterId3":400101,"characterLevel3":2,"characterAwakening3":0,"characterId4":105,"characterLevel4":2,"characterAwakening4":0,"characterId5":104,"characterLevel5":2,"characterAwakening5":0,"achievement":999840,"deluxscore":2178,"scoreRank":11,"maxCombo":807,"totalCombo":807,"maxSync":807,"totalSync":0,"tapCriticalPerfect":411,"tapPerfect":159,"tapGreat":17,"tapGood":1,"tapMiss":0,"holdCriticalPerfect":61,"holdPerfect":27,"holdGreat":0,"holdGood":0,"holdMiss":0,"slideCriticalPerfect":58,"slidePerfect":0,"slideGreat":0,"slideGood":2,"slideMiss":0,"touchCriticalPerfect":49,"touchPerfect":0,"touchGreat":0,"touchGood":0,"touchMiss":0,"breakCriticalPerfect":9,"breakPerfect":12,"breakGreat":1,"breakGood":0,"breakMiss":0,"isTap":true,"isHold":true,"isSlide":true,"isTouch":true,"isBreak":true,"isCriticalDisp":true,"isFastLateDisp":true,"fastCount":8,"lateCount":13,"isAchieveNewRecord":true,"isDeluxscoreNewRecord":true,"comboStatus":1,"syncStatus":0,"isClear":true,"beforeRating":271,"afterRating":524,"beforeGrade":0,"afterGrade":0,"afterGradeRank":0,"beforeDeluxRating":271,"afterDeluxRating":524,"isPlayTutorial":true,"isEventMode":false,"isFreedomMode":false,"playMode":0,"isNewFree":false,"trialPlayAchievement":-1,"extNum1":0,"extNum2":0,"extNum4":106,"extBool1":false}}""").let { (_, result) ->
            result shouldBe """{"returnCode":1,"apiName":"com.sega.maimai2servlet.api.UploadUserPlaylogApi"}""".jsonMap()
        }
    }

    "UpsertUserAllApi2" {
        post("UpsertUserAllApi", """{"userId":$userId,"playlogId":1,"isEventMode":false,"isFreePlay":true,"upsertUserAll":{"userData":[{"accessCode":"$ACCESS_CODE","userName":"ＡＺＡ☆","isNetMember":1,"iconId":11,"plateId":1,"titleId":11,"partnerId":1,"frameId":1,"selectMapId":400001,"totalAwake":0,"gradeRating":0,"musicRating":524,"playerRating":524,"highestRating":524,"gradeRank":0,"classRank":0,"courseRank":0,"charaSlot":[101,102,400101,105,104],"charaLockSlot":[0,0,0,0,0],"contentBit":6364192,"playCount":2,"currentPlayCount":2,"renameCredit":0,"mapStock":5000,"eventWatchedDate":"2024-03-27 05:57:16.0","lastGameId":"SDEZ","lastRomVersion":"1.41.00","lastDataVersion":"1.40.08","lastLoginDate":"2024-03-27 05:57:16.0","lastPlayDate":"2024-03-27 06:02:48.0","lastPlayCredit":1,"lastPlayMode":0,"lastPlaceId":291,"lastPlaceName":"","lastAllNetId":0,"lastRegionId":1,"lastRegionName":"W","lastClientId":"$CLIENT_ID","lastCountryCode":"JPN","lastSelectEMoney":0,"lastSelectTicket":0,"lastSelectCourse":0,"lastCountCourse":0,"firstGameId":"SDEZ","firstRomVersion":"1.41.00","firstDataVersion":"1.40.08","firstPlayDate":"2024-03-27 05:45:15.0","compatibleCmVersion":"1.40.00","dailyBonusDate":"1970-01-01 09:00:00.0","dailyCourseBonusDate":"1970-01-01 09:00:00.0","lastPairLoginDate":"1970-01-01 09:00:00.0","lastTrialPlayDate":"1970-01-01 09:00:00.0","playVsCount":0,"playSyncCount":0,"winCount":0,"helpCount":0,"comboCount":0,"totalDeluxscore":3903,"totalBasicDeluxscore":0,"totalAdvancedDeluxscore":0,"totalExpertDeluxscore":3903,"totalMasterDeluxscore":0,"totalReMasterDeluxscore":0,"totalSync":0,"totalBasicSync":0,"totalAdvancedSync":0,"totalExpertSync":0,"totalMasterSync":0,"totalReMasterSync":0,"totalAchievement":1997296,"totalBasicAchievement":0,"totalAdvancedAchievement":0,"totalExpertAchievement":1997296,"totalMasterAchievement":0,"totalReMasterAchievement":0,"playerOldRating":524,"playerNewRating":0,"banState":0,"dateTime":1711485182}],"userExtend":[{"selectMusicId":11608,"selectDifficultyId":2,"categoryIndex":195,"musicIndex":96,"extraFlag":561,"selectScoreType":1,"extendContentBit":1,"isPhotoAgree":false,"isGotoCodeRead":false,"selectResultDetails":false,"selectResultScoreViewType":0,"sortCategorySetting":0,"sortMusicSetting":0,"playStatusSetting":0,"selectedCardList":[],"encountMapNpcList":[]}],"userOption":[{"optionKind":3,"noteSpeed":24,"slideSpeed":10,"touchSpeed":21,"tapDesign":0,"holdDesign":0,"slideDesign":0,"starType":0,"outlineDesign":15,"noteSize":1,"slideSize":1,"touchSize":1,"starRotate":1,"dispCenter":1,"outFrameType":2,"dispChain":2,"dispRate":0,"dispBar":0,"touchEffect":0,"submonitorAnimation":2,"submonitorAchive":0,"submonitorAppeal":0,"matching":1,"trackSkip":0,"brightness":0,"mirrorMode":0,"dispJudge":8,"dispJudgePos":5,"dispJudgeTouchPos":1,"adjustTiming":34,"judgeTiming":34,"ansVolume":6,"tapHoldVolume":5,"criticalSe":0,"tapSe":0,"breakSe":0,"breakVolume":5,"exSe":0,"exVolume":5,"slideSe":0,"slideVolume":5,"breakSlideVolume":5,"touchVolume":5,"touchHoldVolume":5,"damageSeVolume":5,"headPhoneVolume":0,"sortTab":0,"sortMusic":0}],"userCharacterList":[{"characterId":101,"level":4,"awakening":0,"useCount":0},{"characterId":102,"level":3,"awakening":0,"useCount":0},{"characterId":104,"level":4,"awakening":0,"useCount":0},{"characterId":105,"level":4,"awakening":0,"useCount":0},{"characterId":400101,"level":4,"awakening":0,"useCount":0},{"characterId":400102,"level":1,"awakening":0,"useCount":0}],"userGhost":[],"userMapList":[{"mapId":400001,"distance":30000,"isLock":false,"isClear":false,"isComplete":false}],"userLoginBonusList":[],"userRatingList":[{"rating":271,"ratingList":[{"musicId":11479,"level":2,"romVersion":23011,"achievement":997456},{"musicId":11176,"level":2,"romVersion":21007,"achievement":999840}],"newRatingList":[],"nextRatingList":[],"nextNewRatingList":[],"udemae":{"rate":0,"maxRate":0,"classValue":0,"maxClassValue":0,"totalWinNum":0,"totalLoseNum":0,"maxWinNum":0,"maxLoseNum":0,"winNum":0,"loseNum":0,"npcTotalWinNum":0,"npcTotalLoseNum":0,"npcMaxWinNum":0,"npcMaxLoseNum":0,"npcWinNum":0,"npcLoseNum":0}}],"userItemList":[{"itemKind":6,"itemId":11479,"stock":1,"isValid":true},{"itemKind":6,"itemId":11176,"stock":1,"isValid":true}],"userMusicDetailList":[{"musicId":11176,"level":2,"playCount":1,"achievement":999840,"comboStatus":1,"syncStatus":0,"deluxscoreMax":2178,"scoreRank":11,"extNum1":0}],"userCourseList":[],"userFriendSeasonRankingList":[{"seasonId":2015,"point":10,"rank":0,"rewardGet":false,"userName":"ＡＺＡ☆","recordDate":"2024-03-27 06:02:34.0"}],"userChargeList":[],"userFavoriteList":[],"userActivityList":[{"playList":[{"kind":1,"id":30,"sortNumber":1711486333,"param1":11479,"param2":2,"param3":129,"param4":0},{"kind":1,"id":23,"sortNumber":1711486333,"param1":11479,"param2":2,"param3":129,"param4":0},{"kind":1,"id":90,"sortNumber":1711486030,"param1":400005,"param2":0,"param3":0,"param4":0},{"kind":1,"id":10,"sortNumber":1711485937,"param1":0,"param2":0,"param3":0,"param4":0}],"musicList":[{"kind":2,"id":11479,"sortNumber":1711486333,"param1":0,"param2":0,"param3":0,"param4":0},{"kind":2,"id":11176,"sortNumber":1711486905,"param1":0,"param2":0,"param3":0,"param4":0}]}],"userGamePlaylogList":[{"playlogId":1,"version":"1.41.00","playDate":"2024-03-27 06:02:48.0","playMode":0,"useTicketId":-1,"playCredit":1,"playTrack":1,"clientId":"$CLIENT_ID","isPlayTutorial":true,"isEventMode":false,"isNewFree":false,"playCount":190,"playSpecial":1642254544,"playOtherUserId":0}],"user2pPlaylog":{"userId1":0,"userId2":0,"userName1":"","userName2":"","regionId":0,"placeId":0,"user2pPlaylogDetailList":[]},"isNewCharacterList":"000001","isNewMapList":"0","isNewLoginBonusList":"","isNewItemList":"01","isNewMusicDetailList":"1","isNewCourseList":"","isNewFavoriteList":"","isNewFriendSeasonRankingList":"1"}}""").let { (_, result) ->
            result shouldBe """{"returnCode":1,"apiName":"com.sega.maimai2servlet.api.UpsertUserAllApi"}""".jsonMap()
        }
    }

    "UserLogoutApi2" {
        post("UserLogoutApi", """{"userId":$userId}""").let { (_, result) ->
            result shouldBe """{"returnCode":1,"apiName":"com.sega.maimai2servlet.api.UserLogoutApi"}""".jsonMap()
        }
    }
})