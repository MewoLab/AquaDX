@file:Suppress("UNCHECKED_CAST")

package icu.samnyan.aqua.sega.ongeki

import ext.*
import icu.samnyan.aqua.sega.general.model.CardStatus
import icu.samnyan.aqua.sega.general.model.response.UserRecentRating
import icu.samnyan.aqua.sega.ongeki.model.OgkItemType
import icu.samnyan.aqua.sega.ongeki.model.UserItem
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


fun OngekiController.initUser() {
    "GetUserData" { mapOf("userId" to uid, "userData" to db.data.findByCard_ExtId(uid)()) }

    "GetUserOption" { mapOf("userId" to uid, "userOption" to db.option.findSingleByUser_Card_ExtId(uid)()) }
    "GetUserEventMap" { mapOf("userId" to uid, "userEventMap" to db.eventMap.findSingleByUser_Card_ExtId(uid)()) }

    "GetUserTechEvent".unpaged { db.techEvent.findByUser_Card_ExtId(uid) }
    "GetUserBoss".unpaged { db.boss.findByUser_Card_ExtId(uid) }
    "GetUserCard".unpaged { db.card.findByUser_Card_ExtId(uid) }
    "GetUserChapter".unpaged { db.chapter.findByUser_Card_ExtId(uid) }
    "GetUserMemoryChapter".unpaged { db.memoryChapter.findByUser_Card_ExtId(uid) }
    "GetUserCharacter".unpaged { db.character.findByUser_Card_ExtId(uid) }
    "GetUserDeckByKey".unpaged("userDeckList") { db.deck.findByUser_Card_ExtId(uid) }
    "GetUserEventMusic".unpaged { db.eventMusic.findByUser_Card_ExtId(uid) }
    "GetUserEventPoint".unpaged { db.eventPoint.findByUser_Card_ExtId(uid) }
    "GetUserKop".unpaged { db.kop.findByUser_Card_ExtId(uid) }
    "GetUserLoginBonus".unpaged { db.loginBonus.findByUser_Card_ExtId(uid) }
    "GetUserMissionPoint".unpaged { db.missionPoint.findByUser_Card_ExtId(uid) }
    "GetUserMusicItem".unpaged { db.musicItem.findByUser_Card_ExtId(uid) }
    "GetUserRival".unpaged { db.rivalData.findByUser_Card_ExtId(uid) }
    "GetUserScenario".unpaged { db.scenario.findByUser_Card_ExtId(uid) }
    "GetUserSkin".unpaged { db.skin.findByUser_Card_ExtId(uid) }
    "GetUserStory".unpaged { db.story.findByUser_Card_ExtId(uid) }
    "GetUserTechCount".unpaged { db.techCount.findByUser_Card_ExtId(uid) }
    "GetUserTrainingRoomByKey".unpaged("userTrainingRoomList") { db.trainingRoom.findByUser_Card_ExtId(uid) }

    "GetUserBpBase".unpaged { empty }
    "GetUserRatinglog".unpaged { empty }
    "GetUserRegion".unpaged {
        db.regions.findByUser_Card_ExtId(uid)
            .map { mapOf("regionId" to it.regionId, "playCount" to it.playCount) }
    }

    "GetUserTradeItem".unpaged {
        val start = parsing { data["startChapterId"]!!.int }
        val end = parsing { data["endChapterId"]!!.int }

        db.tradeItem.findByUser_Card_ExtIdAndChapterIdGreaterThanEqualAndChapterIdLessThanEqual(uid, start, end)
    }

    val dtPattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.0")

    "GetUserTechEventRanking".unpaged {
        val time = LocalDateTime.now().format(dtPattern)

        db.techEvent.findByUser_Card_ExtId(uid).map { mapOf(
            "eventId" to it.eventId,
            "date" to time,
            "rank" to 1,
            "totalTechScore" to it.totalTechScore,
            "totalPlatinumScore" to it.totalPlatinumScore
        ) }
    }

    "GetUserEventRanking".unpaged {
        val time = LocalDateTime.now().format(dtPattern)

        db.eventPoint.findByUser_Card_ExtId(uid).map { mapOf(
            "eventId" to it.eventId,
            "type" to 1, // Latest ranking
            "date" to time,
            "rank" to db.eventPoint.calculateRankByUserAndEventId(uid, it.eventId),
            "point" to it.point
        ) }
    }

    "GetUserActivity".unpagedExtra {
        val kind = parsing { data["kind"]!!.int }
        db.activity.findByUser_Card_ExtIdAndKindOrderBySortNumberDesc(uid, kind)
            .take(if (kind == 1) 15 else 10) to mapOf("kind" to kind)
    }

    "GetUserItem" {
        val kind = (parsing { data["nextIndex"]!!.long } / 10000000000L).toInt()
        var dat = db.item.findByUser_Card_ExtIdAndItemKind(uid, kind)

        // Check if user have infinite kaika
        if (kind == OgkItemType.KaikaItem.ordinal) {
            val u = db.data.findByCard_ExtId(uid)()
            u?.card?.aquaUser?.gameOptions?.let {
                if (it.ongekiInfiniteKaika) {
                    dat = listOf(UserItem().apply {
                        user = u
                        itemKind = OgkItemType.KaikaItem.ordinal
                        itemId = 1
                        stock = 999
                    })
                }
            }
        }

        mapOf("userId" to uid, "length" to dat.size, "nextIndex" to -1, "itemKind" to kind, "userItemList" to dat)
    }

    "GetUserMusic" {
        val dat = db.musicDetail.findByUser_Card_ExtId(uid).groupBy { it.musicId }
            .mapValues { mapOf("length" to it.value.size, "userMusicDetailList" to it.value) }
            .values.toList()
        mapOf("userId" to uid, "length" to dat.size, "nextIndex" to -1, "userMusicList" to dat)
    }

    "GetUserPreview" api@ {
	    val u = db.data.findByCard_ExtId(uid)() ?: return@api mapOf(
		    "userId" to uid,
		    "isLogin" to false,
		    "lastLoginDate" to "0000-00-00 00:00:00",
		    "userName" to "",
		    "reincarnationNum" to 0,
		    "level" to 0,
		    "exp" to 0,
		    "playerRating" to 0,
		    "lastGameId" to "",
		    "lastRomVersion" to "",
		    "lastDataVersion" to "",
		    "lastPlayDate" to "",
		    "nameplateId" to 0,
		    "trophyId" to 0,
		    "cardId" to 0,
		    "dispPlayerLv" to 0,
		    "dispRating" to 0,
		    "dispBP" to 0,
		    "headphone" to 0,
		    "banStatus" to 0,
		    "isWarningConfirmed" to true
	    )
        val o = db.option.findSingleByUser(u)()

        val res = mutableMapOf(
            "userId" to uid, "isLogin" to false,

            "userName" to u.userName, "reincarnationNum" to u.reincarnationNum,
            "level" to u.level, "exp" to u.exp, "playerRating" to u.playerRating, "newPlayerRating" to u.newPlayerRating,
            "lastGameId" to u.lastGameId, "lastRomVersion" to u.lastRomVersion, "lastDataVersion" to u.lastDataVersion,
            "lastPlayDate" to u.lastPlayDate, "lastLoginDate" to u.lastPlayDate,
            "nameplateId" to u.nameplateId, "trophyId" to u.trophyId, "cardId" to u.cardId,

            "dispPlayerLv" to (o?.dispPlayerLv ?: 1),
            "dispRating" to (o?.dispRating ?: 1),
            "dispBP" to (o?.dispBP ?: 1),
            "headphone" to (o?.headphone ?: 0),

            "lastEmoneyBrand" to 4,
            "lastEmoneyCredit" to 10000,

            "banStatus" to 0,
            "isWarningConfirmed" to false,
        )

        if (u.card?.status == CardStatus.MIGRATED_TO_MINATO) {
            res["userName"] = "Migrated"
            res["level"] = 0
            res["exp"] = 0
            res["playerRating"] = 0
            res["newPlayerRating"] = 0
        }

        res
    }

    "GameLogin" {
        val user = db.data.findByCard_ExtId(uid)()

        if (user?.card?.status == CardStatus.MIGRATED_TO_MINATO) {
            """{"returnCode":"0"}"""
        }
        else {
            """{"returnCode":"1"}"""
        }
    }

    "GetUserRecentRating".unpaged {
        db.generalData.findByUser_Card_ExtIdAndPropertyKey(uid, "recent_rating_list")()?.let { recent ->
            recent.propertyValue.split(',').dropLastWhile { it.isEmpty() }.map {
                val (m, d, s) = it.split(':').map { it.int }
                UserRecentRating(m, d, "1000000", s)
            }
        } ?: run {
            db.playlog.findByUser_Card_ExtId(uid, PageRequest.of(0, 30, Sort.by(Sort.Direction.DESC, "id"))).content
                .map { UserRecentRating(it.musicId, it.level, "1000000", it.techScore) }
        }
    }

    "GetUserRivalData" {
        val idList = (data["userRivalList"] as Collection<JDict>)
            .map { it["rivalUserId"]!!.long }

        db.data.findByCard_ExtIdIn(idList)
            .map { mapOf("rivalUserId" to it.card!!.extId, "rivalUserName" to it.userName) }
    }

    "GetUserRivalMusic".unpagedExtra {
        val rivalUserId = (data["rivalUserId"] as Number).toLong()

        val l = db.musicDetail.findByUser_Card_ExtId(rivalUserId).groupBy { it.musicId }
            .map { mapOf("userRivalMusicDetailList" to it.value, "length" to it.value.size) }

        l to mapOf("rivalUserId" to rivalUserId)
    }
}
