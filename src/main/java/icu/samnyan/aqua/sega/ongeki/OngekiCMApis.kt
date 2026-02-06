package icu.samnyan.aqua.sega.ongeki

import ext.*
import icu.samnyan.aqua.sega.ongeki.model.*
import java.time.LocalDateTime

fun OngekiController.cmApiInit() {
    "CMGetUserData" {
        val user = db.data.findByCard_ExtId(uid) ?: (400 - "User not found")
        mapOf("userId" to uid, "userData" to user)
    }

    "PrinterLogin" { mapOf("returnCode" to 1) }
    "PrinterLogout" { mapOf("returnCode" to 1) }

    "CMGetUserCard".unpaged("userCardList"){
        db.card.findByUser_Card_ExtId(uid).map { it.apply { printCount = 99 } }
    }

    "CMGetUserCharacter".unpaged("userCharacterList") { db.character.findByUser_Card_ExtId(uid)}

    "CMGetUserItem" {
        val kind = (parsing { data["nextIndex"]!!.long } / 10000000000L).toInt()
        var dat = db.item.findByUser_Card_ExtIdAndItemKind(uid, kind)

        // Check if user have infinite kaika
        if (kind == OgkItemType.KaikaItem.ordinal) {
            val u = db.data.findByCard_ExtId(uid)
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

    "GetGameGacha" {
        gdb.gacha.findAll().let {
            mapOf("length" to it.size, "gameGachaList" to it, "registIdList" to emptyList<Any>())
        }
    }

    "GetGameGachaCardById" {
        val gachaId = parsing { data["gachaId"]!!.long }

        gdb.gachaCard.findAllByGachaId(gachaId).let {
            mapOf("gachaId" to gachaId, "length" to it.size, "isPickup" to false, "gameGachaCardList" to it,
                "emissionList" to empty, "afterCalcList" to empty
            )
        }
    }

    "GetUserGacha" {
        db.gacha.findByUser_Card_ExtId(uid).let{
            mapOf("userId" to uid, "length" to it.size, "nextIndex" to 0, "userGachaList" to it)
        }
    }

    "RollGacha" {
        val (gachaId, tmpTimes) = parsing { data["gachaId"]!!.long to data["times"]!!.int }
        var times = tmpTimes

        val user = db.data.findByCard_ExtId(uid) ?: (400 - "User not found")
        val foundGacha = gdb.gacha.findById(gachaId)() ?: (404 - "Gacha not found")

        val foundUserGacha = db.gacha.findByUserAndGachaId(user, gachaId)

        // Official gacha percentages: R 76% SR 21% SSR 3%
        val probabilities = listOf(76, 21, 3)
        var rarityResults: List<Int> = emptyList()

        // If this is the first x5 pull or any x11 pull, an SR is guaranteed
        if(foundUserGacha != null && ((times == 5 && foundUserGacha.fiveGachaCnt==0) || times==11)){
            rarityResults = listOf(3)
            times -= 1
        }

        rarityResults = rarityResults + List(times) {
            val rand = (1..100).random()
            when {
                rand <= probabilities[0] -> 2
                rand <= probabilities[0] + probabilities[1] -> 3
                else -> 4
            }
        }

        val pulledCards = if(foundGacha.kind==0 || foundGacha.kind==3) {
            // If it is the free or the permanent gacha, include cards from the gacha itself
            gdb.gachaCard.findAllByGachaId(gachaId).groupBy { it.rarity }
        } else {
            // If it is a normal gacha, include all cards from the permanent gacha
            gdb.gachaCard.findAllByGachaIdAndPermanent(gachaId).groupBy { it.rarity }
        }

        val finalPulls = rarityResults.map { rarity -> pulledCards[rarity]?.random()}

        mapOf("length" to finalPulls.size, "gameGachaCardList" to finalPulls)
    }

    "CMUpsertUserGacha" api@ {
        val all: OngekiCMUpsertUserGacha = mapper.convert(data["cmUpsertUserGacha"]!!)
        val gacha = parsing { data["gachaId"]!!.long }
        val pullCount = parsing { data["gachaCnt"]!!.int }
        val earnedSelectPoints = parsing { data["selectPoint"]!!.int }

        // User data
        val oldUser = db.data.findByCard_ExtId(uid)
        val u: UserData = all.userData?.get(0)?.apply {
            id = oldUser?.id ?: 0
            card = oldUser?.card ?: us.cardRepo.findByExtId(uid) ?: (404 - "Card not found")

            // Set eventWatchedDate with lastPlayDate, because client doesn't update it
            cmEventWatchedDate = oldUser?.lastPlayDate ?: ""
            db.data.save(this)
        } ?: oldUser ?: return@api null

        db.gacha.findByUserAndGachaId(u, gacha)?.apply {
            totalGachaCnt += pullCount

            // If the user pulled a select gacha increase the stats
            if (earnedSelectPoints > 0) {
                selectPoint += earnedSelectPoints
                ceilingGachaCnt += pullCount
            }

            // Stats related with the group of pulls the user makes
            if (pullCount == 5) {
                fiveGachaCnt += 1
            }else if (pullCount == 11) {
                elevenGachaCnt+=1
            }

            // Update the daily gacha
            val parsedDailyGachaDate = dailyGachaDate.asDateTime()!!
            if (
                parsedDailyGachaDate.dayOfMonth != LocalDateTime.now().dayOfMonth ||
                parsedDailyGachaDate.monthValue != LocalDateTime.now().monthValue ||
                parsedDailyGachaDate.year != LocalDateTime.now().year
                ) {
                dailyGachaCnt += pullCount
                dailyGachaDate = LocalDateTime.now().toString()
            }else{
                dailyGachaCnt += pullCount
            }
        }?: UserGacha().apply {
            user = u
            gachaId = gacha
            totalGachaCnt = pullCount
            selectPoint = earnedSelectPoints
            ceilingGachaCnt = if (earnedSelectPoints > 0) 1 else 0
            fiveGachaCnt = if (pullCount == 5) 1 else 0
            elevenGachaCnt = if (pullCount == 11) 1 else 0
            dailyGachaCnt = pullCount

            db.gacha.save(this)
        }

        all.run {
            // Set users
            listOfNotNull(
                userCharacterList, userCardList, userItemList
            ).flatten().forEach { it.user = u }

            // UserCharacterList
            userCharacterList?.let { list ->
                db.character.saveAll(list.distinctBy { it.characterId }.mapApply {
                    id = db.character.findByUserAndCharacterId(u, characterId)?.id ?: 0
                })
            }

            // UserCardList
            userCardList?.let { list ->
                db.card.saveAll(list.distinctBy { it.cardId }.mapApply {
                    id = db.card.findByUserAndCardId(u, cardId)?.id ?: 0
                })
            }

            // UserItemList
            userItemList?.let { list ->
                db.item.saveAll(list.distinctBy { it.itemKind to it.itemId }.mapApply {
                    id = db.item.findByUserAndItemKindAndItemId(u, itemKind, itemId)?.id ?: 0
                })
            }
        }

        u.card?.let { cardService.updateCardTimestamp(it, "ongeki") }

        null
    }

    "CMUpsertUserAll" api@{
        val all: OngekiCMUpsertAll = mapper.convert(data["cmUpsertUserAll"]!!)

        // User data
        val oldUser = db.data.findByCard_ExtId(uid)
        val u: UserData = all.userData?.get(0)?.apply {
            id = oldUser?.id ?: 0
            card = oldUser?.card ?: us.cardRepo.findByExtId(uid) ?: (404 - "Card not found")

            // Set eventWatchedDate with lastPlayDate, because client doesn't update it
            cmEventWatchedDate = oldUser?.lastPlayDate ?: ""
            db.data.save(this)
        } ?: oldUser ?: return@api null

        all.run {
            // Set users
            listOfNotNull(
                userActivityList, userCharacterList, userCardList, userItemList
            ).flatten().forEach { it.user = u }

            // UserActivityList
            userActivityList?.let { list ->
                db.activity.saveAll(list.distinctBy { it.activityId to it.kind }.mapApply {
                    id = db.activity.findByUserAndKindAndActivityId(u, kind, activityId)?.id ?: 0
                })
            }


            // UserCharacterList
            userCharacterList?.let { list ->
                db.character.saveAll(list.distinctBy { it.characterId }.mapApply {
                    id = db.character.findByUserAndCharacterId(u, characterId)?.id ?: 0
                })
            }

            // UserCardList
            userCardList?.let { list ->
                db.card.saveAll(list.distinctBy { it.cardId }.mapApply {
                    id = db.card.findByUserAndCardId(u, cardId)?.id ?: 0
                })
            }

            // UserItemList
            userItemList?.let { list ->
                db.item.saveAll(list.distinctBy { it.itemKind to it.itemId }.mapApply {
                    id = db.item.findByUserAndItemKindAndItemId(u, itemKind, itemId)?.id ?: 0
                })
            }
        }

        u.card?.let { cardService.updateCardTimestamp(it, "ongeki") }

        null
    }

    "CMUpsertUserSelectGacha" api@ {
        val all: OngekiCMUpsertUserSelectGacha = mapper.convert(data["cmUpsertUserSelectGacha"]!!)
        val selectLog:List<OngekiCMSelectGachaLog> = mapper.convert(data["selectGachaLogList"]!!)

        // User data
        val oldUser = db.data.findByCard_ExtId(uid)
        val u: UserData = all.userData?.get(0)?.apply {
            id = oldUser?.id ?: 0
            card = oldUser?.card ?: us.cardRepo.findByExtId(uid) ?: (404 - "Card not found")

            // Set eventWatchedDate with lastPlayDate, because client doesn't update it
            cmEventWatchedDate = oldUser?.lastPlayDate ?: ""
            db.data.save(this)
        } ?: oldUser ?: return@api null

        if(selectLog.isNotEmpty()) {
            val selectionInfo = selectLog.first()

            db.gacha.findByUserAndGachaId(u, selectionInfo.gachaId)?.apply {
                // Total reset of selectPoints
                selectPoint = 0

                // Set the flag for the selection gacha so that the user cant use it anymore
                useSelectPoint = 1

            } ?: UserGacha().apply {
                // It should be impossible to reach this
                user = u
                gachaId = selectionInfo.gachaId

                db.gacha.save(this)
            }
        }

        all.run {
            // Set users
            listOfNotNull(
                 userCharacterList, userCardList
            ).flatten().forEach { it.user = u }


            if (all.isNewCharacterList?.contains("1") ?: false) {
                // UserCharacterList
                userCharacterList?.let { list ->
                    db.character.saveAll(list.distinctBy { it.characterId }.mapApply {
                        id = db.character.findByUserAndCharacterId(u, characterId)?.id ?: 0
                    })
                }
            }

            if (all.isNewCardList?.contains("1") ?: false) {
                // UserCardList
                userCardList?.let { list ->
                    db.card.saveAll(list.distinctBy { it.cardId }.mapApply {
                        id = db.card.findByUserAndCardId(u, cardId)?.id ?: 0
                    })
                }
            }
        }

        u.card?.let { cardService.updateCardTimestamp(it, "ongeki") }

        null
    }

    "CMUpsertUserPrintPlaylog" api@ {
        // User payment logs, useless
        null
    }

    "CMUpsertUserPrint" api@ {
        // User print information, useless
        null
    }

    "CMUpsertUserPrintlog" api@ {
        // User print logs, useless
        null
    }

    "CMGetUserGachaSupply" {
        // Mock function, not sure of functionality

        mapOf("supplyId" to 0, "length" to 0, "supplyCardList" to emptyList<Any>())
    }

    "GetGameTheater" {
        // Mock function, not sure of functionality

        mapOf("length" to 0, "gameTheaterList" to emptyList<Any>(), "registIdList" to emptyList<Any>())
    }
}
