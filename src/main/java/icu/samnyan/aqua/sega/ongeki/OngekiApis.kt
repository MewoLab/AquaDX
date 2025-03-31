package icu.samnyan.aqua.sega.ongeki

import ext.empty
import ext.int
import ext.parsing
import ext.plus

fun OngekiController.ongekiInit() {
    fun <T> List<T>.staticLst(key: String) = mapOf("length" to size, key to this)

    initUser()
    initUpsertAll()

    // Has type, but type is always 1
    "GetGameEvent".static {
        gdb.event.findAll().map {
            mapOf("id" to it.id, "type" to 1, "startDate" to "2005-01-01 00:00:00.0", "endDate" to "2099-01-01 05:00:00.0")
        }.staticLst("gameEventList") + mapOf("type" to 1)
    }

    "GetGamePoint".static { gdb.point.findAll().staticLst("gamePointList") }
    "GetGamePresent".static { gdb.present.findAll().staticLst("gamePresentList") }
    "GetGameReward".static { gdb.reward.findAll().staticLst("gameRewardList") }

    // Dummy endpoints
    "GetGameTechMusic".static { empty.staticLst("gameTechMusicList") }
    "GetGameMessage" { mapOf("type" to data["type"], "length" to 0, "gameMessageList" to empty) }
    "GetGameMusicReleaseState".static { mapOf("techScore" to 0, "cardNum" to 0) }

    "GetGameIdlist" {
        // type 1: Music NG List, 2: Music Recommend List
        val type = parsing { data["type"]!!.int }
        empty.staticLst("gameIdlistList") + mapOf("type" to type)
    }

    "GetGameRanking" {
        // type 1: Music current ranking, 2: Music past ranking
        val type = parsing { data["type"]!!.int }
        empty.staticLst("gameRankingList") + mapOf("type" to type)
    }

    "GetGameSetting" {
        val ver = (data["version"] ?: "1.50.00")
        mapOf(
            "isAou" to false,
            "isDumpUpload" to false,
            "gameSetting" to mapOf(
                "dataVersion" to ver,
                "onlineDataVersion" to ver,
                "isMaintenance" to false,
                "requestInterval" to 10,
                "rebootStartTime" to "2020-01-01 23:59:00.0",
                "rebootEndTime" to "2020-01-01 23:59:00.0",
                "isBackgroundDistribute" to false,
                "maxCountCharacter" to 50,
                "maxCountCard" to 300,
                "maxCountItem" to 300,
                "maxCountMusic" to 50,
                "maxCountMusicItem" to 300,
                "maxCountRivalMusic" to 300
            )
        )
    }

    "GetClientBookkeeping" {
        empty.staticLst("clientBookkeepingList") + mapOf("placeId" to data["placeId"])
    }

    "GetClientTestmode" {
        empty.staticLst("clientTestmodeList") + mapOf("placeId" to data["placeId"])
    }
}