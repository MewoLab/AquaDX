package icu.samnyan.aqua.sega.ongeki

import ext.invoke
import ext.mapApply
import ext.minus
import icu.samnyan.aqua.sega.ongeki.model.OngekiUpsertUserAll
import icu.samnyan.aqua.sega.ongeki.model.UserData
import icu.samnyan.aqua.sega.ongeki.model.UserGeneralData


fun OngekiController.initUpsertAll() {
    fun saveGeneralData(items: List<Any>, u: UserData, key: String) {
        db.generalData.save(UserGeneralData().apply { 
            id = db.generalData.findByUserAndPropertyKey(u, key)()?.id ?: 0
            user = u
            propertyKey = key
            propertyValue = items.joinToString(",")
        })
    }
    
    "UpsertUserAll" api@ {
        val all: OngekiUpsertUserAll = mapper.convert(data["upsertUserAll"]!!)

        // User data
        val oldUser = db.data.findByCard_ExtId(uid)()
        val u: UserData = all.userData?.get(0)?.apply {
            id = oldUser?.id ?: 0
            card = oldUser?.card ?: us.cardRepo.findByExtId(uid)() ?: (404 - "Card not found")

            // Set eventWatchedDate with lastPlayDate, because client doesn't update it
            eventWatchedDate = oldUser?.lastPlayDate ?: ""
            cmEventWatchedDate = oldUser?.lastPlayDate ?: ""
            db.data.save(this)
        } ?: oldUser ?: return@api null

        all.run {
            // Set users
            listOfNotNull(
                userOption, userPlaylogList, userActivityList, userMusicDetailList, userCharacterList, userCardList,
                userDeckList, userTrainingRoomList, userStoryList, userChapterList, userMemoryChapterList, userItemList,
                userMusicItemList, userLoginBonusList, userEventPointList, userMissionPointList, userBossList,
                userTechCountList, userScenarioList, userTradeItemList, userEventMusicList, userTechEventList, userKopList,
                userEventMap?.let { listOf(it) }
            ).flatten().forEach { it.user = u }
            
            // UserOption
            userOption?.get(0)?.let { 
                db.option.save(it.apply {
                    id = db.option.findSingleByUser(u)()?.id ?: 0 }) }

            // UserEventMap
            userEventMap?.let {
                db.eventMap.save(it.apply {
                    id = db.eventMap.findSingleByUser(u)()?.id ?: 0 }) }
            
            // UserPlaylogList
            userPlaylogList?.let { db.playlog.saveAll(it) }

            // UserSessionlogList, UserJewelboostlogLost doesn't need to be saved for a private server

            // Ratings
            mapOf(
                "recent_rating_list" to userRecentRatingList, // This thing still need to save to solve the rating drop
                "battle_point_base" to userBpBaseList, // For calculating Battle point
                "rating_base_best" to userRatingBaseBestList, // This is the best rating of all charts. Best 30 + 10 after that.
                "rating_base_next" to userRatingBaseNextList,
                "rating_base_new_best" to userRatingBaseBestNewList, // This is the best rating of new charts. Best 15 + 10 after that.
                "rating_base_new_next" to userRatingBaseNextNewList,
                "rating_base_hot_best" to userRatingBaseHotList, // This is the recent best
                "rating_base_hot_next" to userRatingBaseHotNextList,

                // Re:Fresh
                "new_rating_base_best" to userNewRatingBaseBestList,
                "new_rating_base_next_best" to userNewRatingBaseNextBestList,
                "new_rating_base_new_best" to userNewRatingBaseBestNewList,
                "new_rating_base_new_next_best" to userNewRatingBaseNextBestNewList,
                "new_rating_base_pscore" to userNewRatingBasePScoreList,
                "new_rating_base_next_pscore" to userNewRatingBaseNextPScoreList
            ).forEach { (k, v) -> v?.let { saveGeneralData(it, u, k) } }

            // UserActivityList
            userActivityList?.let { list ->
                db.activity.saveAll(list.distinctBy { it.activityId to it.kind }.mapApply {
                    id = db.activity.findByUserAndKindAndActivityId(u, kind, activityId)()?.id ?: 0 }) }

            // UserMusicDetailList
            userMusicDetailList?.let { list ->
                db.musicDetail.saveAll(list.distinctBy { it.musicId to it.level }.mapApply {
                    id = db.musicDetail.findByUserAndMusicIdAndLevel(u, musicId, level)()?.id ?: 0 }) }

            // UserCharacterList
            userCharacterList?.let { list ->
                db.character.saveAll(list.distinctBy { it.characterId }.mapApply {
                    id = db.character.findByUserAndCharacterId(u, characterId)()?.id ?: 0 }) }

            // UserCardList
            userCardList?.let { list ->
                db.card.saveAll(list.distinctBy { it.cardId }.mapApply {
                    id = db.card.findByUserAndCardId(u, cardId)()?.id ?: 0 }) }

            // UserDeckList
            userDeckList?.let { list ->
                db.deck.saveAll(list.distinctBy { it.deckId }.mapApply {
                    id = db.deck.findByUserAndDeckId(u, deckId)()?.id ?: 0 }) }

            // UserTrainingRoomList
            userTrainingRoomList?.let { list ->
                db.trainingRoom.saveAll(list.distinctBy { it.roomId }.mapApply {
                    id = db.trainingRoom.findByUserAndRoomId(u, roomId)()?.id ?: 0 }) }

            // UserStoryList
            userStoryList?.let { list ->
                db.story.saveAll(list.distinctBy { it.storyId }.mapApply {
                    id = db.story.findByUserAndStoryId(u, storyId)()?.id ?: 0 }) }

            // UserChapterList
            userChapterList?.let { list ->
                db.chapter.saveAll(list.distinctBy { it.chapterId }.mapApply {
                    id = db.chapter.findByUserAndChapterId(u, chapterId)()?.id ?: 0 }) }

            // UserMemoryChapterList
            userMemoryChapterList?.let { list ->
                db.memoryChapter.saveAll(list.distinctBy { it.chapterId }.mapApply {
                    id = db.memoryChapter.findByUserAndChapterId(u, chapterId)()?.id ?: 0 }) }

            // UserItemList
            userItemList?.let { list ->
                db.item.saveAll(list.distinctBy { it.itemKind to it.itemId }.mapApply {
                    id = db.item.findByUserAndItemKindAndItemId(u, itemKind, itemId)()?.id ?: 0 }) }

            // UserMusicItemList
            userMusicItemList?.let { list ->
                db.musicItem.saveAll(list.distinctBy { it.musicId }.mapApply {
                    id = db.musicItem.findByUserAndMusicId(u, musicId)()?.id ?: 0 }) }

            // UserLoginBonusList
            userLoginBonusList?.let { list ->
                db.loginBonus.saveAll(list.distinctBy { it.bonusId }.mapApply {
                    id = db.loginBonus.findByUserAndBonusId(u, bonusId)()?.id ?: 0 }) }

            // UserEventPointList
            userEventPointList?.let { list ->
                db.eventPoint.saveAll(list.distinctBy { it.eventId }.mapApply {
                    id = db.eventPoint.findByUserAndEventId(u, eventId)()?.id ?: 0 }) }

            // UserMissionPointList
            userMissionPointList?.let { list ->
                db.missionPoint.saveAll(list.distinctBy { it.eventId }.mapApply {
                    id = db.missionPoint.findByUserAndEventId(u, eventId)()?.id ?: 0 }) }

            // UserRatinglogList (For the highest rating of each version)

            // UserBossList
            userBossList?.let { list ->
                db.boss.saveAll(list.distinctBy { it.musicId }.mapApply {
                    id = db.boss.findByUserAndMusicId(u, musicId)()?.id ?: 0 }) }

            // UserTechCountList
            userTechCountList?.let { list ->
                db.techCount.saveAll(list.distinctBy { it.levelId }.mapApply {
                    id = db.techCount.findByUserAndLevelId(u, levelId)()?.id ?: 0 }) }

            // UserScenarioList
            userScenarioList?.let { list ->
                db.scenario.saveAll(list.distinctBy { it.scenarioId }.mapApply {
                    id = db.scenario.findByUserAndScenarioId(u, scenarioId)()?.id ?: 0 }) }

            // UserTradeItemList
            userTradeItemList?.let { list ->
                db.tradeItem.saveAll(list.distinctBy { it.chapterId to it.tradeItemId }.mapApply {
                    id = db.tradeItem.findByUserAndChapterIdAndTradeItemId(u, chapterId, tradeItemId)()?.id ?: 0 }) }

            // UserEventMusicList
            userEventMusicList?.let { list ->
                db.eventMusic.saveAll(list.distinctBy { it.eventId to it.type to it.musicId }.mapApply {
                    id = db.eventMusic.findByUserAndEventIdAndTypeAndMusicId(u, eventId, type, musicId)()?.id ?: 0 }) }

            // UserTechEventList
            userTechEventList?.let { list ->
                db.techEvent.saveAll(list.distinctBy { it.eventId }.mapApply {
                    id = db.techEvent.findByUserAndEventId(u, eventId)()?.id ?: 0 }) }

            // UserKopList
            userKopList?.let { list ->
                db.kop.saveAll(list.distinctBy { it.kopId to it.areaId }.mapApply {
                    id = db.kop.findByUserAndKopIdAndAreaId(u, kopId, areaId)()?.id ?: 0 }) }
        }

        null
    }
}
