package icu.samnyan.aqua.sega.chusan.handler

import ext.*
import icu.samnyan.aqua.sega.chusan.ChusanController
import icu.samnyan.aqua.sega.chusan.model.request.UpsertUserGacha
import icu.samnyan.aqua.sega.chusan.model.request.UserEmoney
import icu.samnyan.aqua.sega.chusan.model.userdata.UserCardPrintState
import icu.samnyan.aqua.sega.chusan.model.userdata.Chu3UserItem
import java.time.LocalDateTime

fun ChusanController.cmApiInit() {
    "CMUpsertUserPrint" { """{"returnCode":1,"orderId":"0","serialId":"FAKECARDIMAG12345678","apiName":"CMUpsertUserPrintApi"}""" }
    "CMUpsertUserPrintlog" { """{"returnCode":1,"orderId":"0","serialId":"FAKECARDIMAG12345678","apiName":"CMUpsertUserPrintlogApi"}""" }

    // CardMaker (TODO: Somebody test this, I don't have a card maker)
    "CMGetUserData" {
        val user = db.userData.findByCard_ExtId(uid)() ?: (400 - "User not found")
        user.userEmoney = UserEmoney()
        mapOf("userId" to uid, "userData" to user, "userEmoney" to user.userEmoney)
    }

    "CMGetUserPreview" {
        val user = db.userData.findByCard_ExtId(uid)() ?: (400 - "User not found")
        mapOf("userName" to user.userName, "level" to user.level, "medal" to user.medal, "lastDataVersion" to user.lastDataVersion, "isLogin" to false)
    }

    "CMUpsertUserGacha" api@ {
        val (gachaId, placeId) = parsing { data["gachaId"]!!.int to data["placeId"]!!.int }

        val u = db.userData.findByCard_ExtId(uid)() ?: return@api null
        val upsertUserGacha = parsing { mapper.convert<UpsertUserGacha>(data["cmUpsertUserGacha"]!!) }

        upsertUserGacha.gameGachaCardList?.let { lst ->
            db.userCardPrintState.saveAll(lst.map {
                UserCardPrintState(
                    hasCompleted = false,
                    limitDate = LocalDateTime.of(2029, 1, 1, 0, 0),
                    placeId = placeId,
                    cardId = it.cardId,
                    gachaId = gachaId
                ).apply { user = u }
            })
        }

        upsertUserGacha.userItemList?.let {
            db.userItem.saveAll(it.mapApply {
                user = u
                id = db.userItem.findByUserAndItemIdAndItemKind(u, itemId, itemKind)?.id ?: 0
            })
        }

        upsertUserGacha.userGacha?.let {
            it.user = u
            db.userGacha.save(it)
        }

        //Set the hasCompleted to true
        val printState = db.userCardPrintState.findByUserAndGachaIdAndHasCompleted(u, gachaId, false)

        if (printState.isEmpty()) return@api null

        printState.forEach { it.hasCompleted = true }
        db.userCardPrintState.saveAll(printState)

        // Append the order ID to the response with the key "orderId"
        val fullPrintState = printState.map {
            mapOf(
                "orderId" to it.id,
                "hasCompleted" to it.hasCompleted,
                "limitDate" to it.limitDate.toString(),
                "placeId" to it.placeId,
                "cardId" to it.cardId,
                "gachaId" to it.gachaId,
                "userId" to uid
            )
        }

        u.card?.let { cardService.updateCardTimestamp(it, "chu3") }

        mapOf(
            "returnCode" to 1,
            "apiName" to "CMUpsertUserGachaApi",
            "userCardPrintStateList" to fullPrintState
        )
    }

    "CMUpsertUserPrintCancel" {
        val orderIdList: List<Long> = cmMapper.convert<List<Long>>(parsing { data["orderIdList"]!! })

        val states = db.userCardPrintState.saveAll(orderIdList.mapNotNull {
            // TODO: The original code by Eori writes findById but I don't think that is correct...
            db.userCardPrintState.findById(it)()?.apply {
                hasCompleted = true
            }
        })

        states.firstOrNull()?.user?.card?.let { cardService.updateCardTimestamp(it, "chu3") }

        mapOf("returnCode" to 1, "apiName" to "CMUpsertUserPrintCancelApi")
    }

    "CMUpsertUserPrintSubtract" api@ {
        val userCardPrintState = cmMapper.convert<UserCardPrintState>(parsing { data["userCardPrintState"]!! })
        val userItemList = cmMapper.convert<List<Chu3UserItem>>(parsing { data["userItemList"]!! })

        val u = db.userData.findByCard_ExtId(uid)() ?: return@api null

        db.userItem.saveAll(
            userItemList.mapApply {
                id = db.userItem.findByUserAndItemIdAndItemKind(u, itemId, itemKind)?.id ?: 0
                user = u
            }
        )

        // TODO: I also doubt this is correct... it shouldn't be ID
        db.userCardPrintState.findById(userCardPrintState.id)()?.apply {
            hasCompleted = true
            db.userCardPrintState.save(this)
        }

        u.card?.let { cardService.updateCardTimestamp(it, "chu3") }

        mapOf("returnCode" to 1, "apiName" to "CMUpsertUserPrintSubtractApi")
    }
}
