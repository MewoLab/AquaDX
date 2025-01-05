package icu.samnyan.aqua.sega.chusan.handler

import ext.*
import icu.samnyan.aqua.sega.chusan.ChusanController
import icu.samnyan.aqua.sega.chusan.model.request.UpsertUserAll
import icu.samnyan.aqua.sega.chusan.model.userdata.*
import icu.samnyan.aqua.sega.general.model.response.UserRecentRating
import java.nio.charset.StandardCharsets
import java.time.LocalDateTime

@Suppress("UNCHECKED_CAST")
fun ChusanController.upsertApiInit() {
    "UpsertUserChargelog" {
        val charge = parsing { mapper.convert<UserCharge>(data["userCharge"] as JDict) }
        charge.user = db.userData.findByCard_ExtId(uid)() ?: (400 - "User not found")
        charge.id = db.userCharge.findByUser_Card_ExtIdAndChargeId(uid, charge.chargeId)?.id ?: 0
        db.userCharge.save(charge)
        """{"returnCode":"1"}"""
    }

    "UpsertUserAll" api@ {
        val req = mapper.convert(data["upsertUserAll"], UpsertUserAll::class.java)

        req.run {
            // UserData
            val oldUser = db.userData.findByCard_ExtId(uid)()
            val u = (userData?.get(0) ?: return@api null).apply {
                id = oldUser?.id ?: 0
                card = oldUser?.card ?: us.cardRepo.findByExtId(uid).expect("Card not found")
                userName = String(userName.toByteArray(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8)
                userNameEx = ""
            }.also { db.userData.saveAndFlush(it) }

            versionHelper[u.lastClientId] = u.lastDataVersion

            // Set users
            listOfNotNull(
                userPlaylogList, userGameOption, userMapAreaList, userCharacterList, userItemList,
                userMusicDetailList, userActivityList, userChargeList, userCourseList, userDuelList,
            ).flatten().forEach { it.user = u }

            // Ratings
            fun Iterable<UserRecentRating>.str() = joinToString(",") { "${it.musicId}:${it.difficultId}:${it.score}" }

            ls(
                userRecentRatingList to "recent_rating_list", userRatingBaseList to "rating_base_list",
                userRatingBaseHotList to "rating_hot_list", userRatingBaseNextList to "rating_next_list",
            ).filter { it.first != null }.forEach { (list, key) ->
                val d = db.userGeneralData.findByUserAndPropertyKey(u, key)()
                    ?: UserGeneralData().apply { user = u; propertyKey = key }
                db.userGeneralData.save(d.apply { propertyValue = list!!.str() })
            }

            userFavoriteMusicList?.filter { it.musicId != -1 }?.let { list ->
                val d = db.userGeneralData.findByUserAndPropertyKey(u, "favorite_music")()
                    ?: UserGeneralData().apply { user = u; propertyKey = "favorite_music" }
                db.userGeneralData.save(d.apply {
                    propertyValue = list.distinct().joinToString(",") { it.musicId.toString() } })
            }

            // Playlog
            userPlaylogList?.let { db.userPlaylog.saveAll(it) }

            // List data
            userGameOption?.get(0)?.let { obj ->
                db.userGameOption.saveAndFlush(obj.apply {
                    id = db.userGameOption.findSingleByUser(u)()?.id ?: 0 }) }

            userMapAreaList?.let { list ->
                db.userMap.saveAll(list.distinctBy { it.mapAreaId }.mapApply {
                    id = db.userMap.findByUserAndMapAreaId(u, mapAreaId)?.id ?: 0 }) }

            userCharacterList?.let { list ->
                db.userCharacter.saveAll(list.distinctBy { it.characterId }.mapApply {
                    id = db.userCharacter.findByUserAndCharacterId(u, characterId)?.id ?: 0 }) }

            userItemList?.let { list ->
                db.userItem.saveAll(list.distinctBy { it.itemId to it.itemKind }.mapApply {
                    id = db.userItem.findByUserAndItemIdAndItemKind(u, itemId, itemKind)?.id ?: 0 }) }

            userMusicDetailList?.let { list ->
                db.userMusicDetail.saveAll(list.distinctBy { it.musicId to it.level }.mapApply {
                    id = db.userMusicDetail.findByUserAndMusicIdAndLevel(u, musicId, level)?.id ?: 0 }) }

            userActivityList?.let { list ->
                db.userActivity.saveAll(list.distinctBy { it.activityId to it.kind }.mapApply {
                    id = db.userActivity.findByUserAndActivityIdAndKind(u, activityId, kind)?.id ?: 0 }) }

            userChargeList?.let { list ->
                db.userCharge.saveAll(list.distinctBy { it.chargeId }.mapApply {
                    id = db.userCharge.findByUserAndChargeId(u, chargeId)()?.id ?: 0 }) }

            userCourseList?.let { list ->
                db.userCourse.saveAll(list.distinctBy { it.courseId }.mapApply {
                    id = db.userCourse.findByUserAndCourseId(u, courseId)?.id ?: 0 }) }

            userDuelList?.let { list ->
                db.userDuel.saveAll(list.distinctBy { it.duelId }.mapApply {
                    id = db.userDuel.findByUserAndDuelId(u, duelId)?.id ?: 0 }) }

            // Need testing
            userLoginBonusList?.let { list ->
                db.userLoginBonus.saveAll(list.distinctBy { it["presetId"] as String }.map {
                    val id = it["presetId"]!!.int
                    (db.userLoginBonus.findLoginBonus(uid.int, 1, id)() ?: UserLoginBonus()).apply {
                        user = u.id.toInt()
                        presetId = id
                        lastUpdateDate = LocalDateTime.now()
                        isWatched = true
                    }
                })
            }

            req.userCMissionList?.forEach { d ->
                (db.userCMission.findByUser_Card_ExtIdAndMissionId(uid, d.missionId)()
                    ?: UserCMission().apply {
                        missionId = d.missionId
                        user = u
                    }
                    ).apply { point = d.point }.also { db.userCMission.save(it) }

                d.userCMissionProgressList?.forEach inner@ { p ->
                    (db.userCMissionProgress.findByUser_Card_ExtIdAndMissionIdAndOrder(uid, d.missionId, p.order)()
                        ?: UserCMissionProgress().apply {
                            missionId = d.missionId
                            order = p.order
                            user = u
                        }
                        ).apply {
                            progress = p.progress
                            stage = p.stage
                        }.also { db.userCMissionProgress.save(it) }
                }
            }
        }

        """{"returnCode":1}"""
    }
}