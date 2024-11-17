package icu.samnyan.aqua.sega.maimai2.handler

import com.fasterxml.jackson.core.JsonProcessingException
import ext.invoke
import ext.mapApply
import ext.minus
import icu.samnyan.aqua.net.games.SEGA_USERNAME_CAHRS
import icu.samnyan.aqua.sega.general.BaseHandler
import icu.samnyan.aqua.sega.general.service.CardService
import icu.samnyan.aqua.sega.maimai2.handler.UploadUserPlaylogHandler.Companion.playBacklog
import icu.samnyan.aqua.sega.maimai2.model.*
import icu.samnyan.aqua.sega.maimai2.model.request.UpsertUserAll
import icu.samnyan.aqua.sega.maimai2.model.userdata.*
import icu.samnyan.aqua.sega.util.jackson.BasicMapper
import lombok.AllArgsConstructor
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import kotlin.math.log

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@AllArgsConstructor
@Component("Maimai2UpsertUserAllHandler")
class UpsertUserAllHandler(
    val mapper: BasicMapper,
    val cardService: CardService,
    val repos: Mai2Repos
) : BaseHandler {

    fun String.isValidUsername() = isNotBlank() && length <= 8

    @Throws(JsonProcessingException::class)
    override fun handle(request: Map<String, Any>): Any? {
        val upsertUserAll = mapper.convert(request, UpsertUserAll::class.java)
        val userId = upsertUserAll.userId
        val req = upsertUserAll.upsertUserAll

        // If user is guest, just return OK response.
        if ((userId and 281474976710657L) == 281474976710657L) return SUCCESS

        // UserData
        if (req.userData == null) 400 - "Invalid Request"

        val userData = repos.userData.findByCardExtId(userId)()
        val u = repos.userData.saveAndFlush(req.userData[0].apply {
            id = userData?.id ?: 0
            card = userData?.card ?: cardService.getCardByExtId(userId).orElseThrow()
            isNetMember = 1

            // Validate username
            if (!userName.isValidUsername())
            {
                // Maybe it's encoded
                val decodeUserName = String(userName.toByteArray(Charsets.ISO_8859_1), Charsets.UTF_8)
                if (userName.isValidUsername()) {
                    userName = decodeUserName
                } else {
                    logger.warn("Invalid username: $userName")
                    // Maybe it's from some self-made import tools or modified game
                    // So apply it if user not in database
                    userName = userData?.userName ?: userName
                }
            }
        })

        // Check playlog backlog
        if (playBacklog.containsKey(userId)) playBacklog.remove(userId)?.forEach {
            repos.userPlaylog.save(it.playlog.apply { user = u })
        }

        // Set users
        req.run { listOf(userExtend, userOption, userCharacterList, userMapList, userLoginBonusList, userItemList,
            userMusicDetailList, userCourseList, userFriendSeasonRankingList, userFavoriteList).filterNotNull() }
            .flatten().forEach {
                logger.info(it.toString())
                it.user = u
            }

        req.userExtend?.getOrNull(0)?.let {
            repos.userExtend.save(it.apply { id = repos.userExtend.findSingleByUser(u)()?.id ?: 0 })
        }

        req.userOption?.getOrNull(0)?.let {
            repos.userOption.save(it.apply { id = repos.userOption.findSingleByUser(u)()?.id ?: 0 })
        }

        req.userCharacterList?.let { news ->
            repos.userCharacter.saveAll(news.mapApply {
                id = repos.userCharacter.findByUserAndCharacterId(u, characterId)()?.id ?: 0 }) }

        req.userMapList?.let { news ->
            repos.userMap.saveAll(news.mapApply {
                id = repos.userMap.findByUserAndMapId(u, mapId)()?.id ?: 0 }) }

        req.userLoginBonusList?.let { news ->
            repos.userLoginBonus.saveAll(news.mapApply {
                id = repos.userLoginBonus.findByUserAndBonusId(u, bonusId)()?.id ?: 0 }) }

        req.userRatingList?.getOrNull(0)?.let { r ->
            repos.userUdemae.saveAndFlush(r.udemae.apply {
                id = repos.userUdemae.findSingleByUser(u)()?.id ?: 0
                user = u
            })

            saveRating(r.ratingList, u, "recent_rating")
            saveRating(r.newRatingList, u, "recent_rating_new")
            saveRating(r.nextRatingList, u, "recent_rating_next")
            saveRating(r.nextNewRatingList, u, "recent_rating_next_new")
        }

        req.userItemList?.let { news ->
            repos.userItem.saveAll(news.mapApply {
                id = repos.userItem.findByUserAndItemKindAndItemId(u, itemKind, itemId)()?.id ?: 0 }) }

        req.userMusicDetailList?.let { news ->
            repos.userMusicDetail.saveAll(news.mapApply {
                id = repos.userMusicDetail.findByUserAndMusicIdAndLevel(u, musicId, level)()?.id ?: 0 }) }

        req.userCourseList?.let { news ->
            repos.userCourse.saveAll(news.mapApply {
                id = repos.userCourse.findByUserAndCourseId(u, courseId)()?.id ?: 0 }) }

        req.userFriendSeasonRankingList?.let { news ->
            repos.userFriendSeasonRanking.saveAll(news.mapApply {
                id = repos.userFriendSeasonRanking.findByUserAndSeasonId(u, seasonId)()?.id ?: 0 }) }

        req.userFavoriteList?.let { news ->
            repos.userFavorite.saveAll(news.mapApply {
                id = repos.userFavorite.findByUserAndItemKind(u, itemKind)()?.id ?: 0 }) }

        // 2024/10/31 Found some user data findByUserAndKindAndActivityId is not unique
        // I think userActivityList is not important, so I will ignore it
        try {
            req.userActivityList?.let { news ->
                repos.userAct.saveAll(news.flatMap { listOf(it.musicList, it.playList) }.flatten()
                    .filter { it.kind != 0 && it.activityId != 0 }
                    .mapApply {
                        id = repos.userAct.findByUserAndKindAndActivityId(u, kind, activityId)()?.id ?: 0
                        user = u
                    }.sortedBy { it.sortNumber })
            }
        } catch (e: Exception) {
            logger.error("Error saving user activity", e)
        }

        if(req.isNewFavoritemusicList == "0")
            // According to code, 0 here represents favourite differ and will send complete new list via userFavoritemusicList.
            // Or userFavoritemusicList will be empty
            req.userFavoritemusicList?.let { news ->
                val key = "favorite_music"
                val data = repos.userGeneralData.findByUserAndPropertyKey(u, key)()
                    ?: Mai2UserGeneralData().apply { user = u; propertyKey = key }
                repos.userGeneralData.save(data.apply {
                    propertyValue = news.map { it.id }.joinToString(",")
                })
            }

        return SUCCESS
    }

    fun saveRating(itemList: List<Mai2UserRate>, u: Mai2UserDetail, key: String) {
        val sb = itemList.joinToString(",") { "${it.musicId}:${it.level}:${it.romVersion}:${it.achievement}" }
        val data = repos.userGeneralData.findByUserAndPropertyKey(u, key)()
            ?: Mai2UserGeneralData().apply { user = u; propertyKey = key }
        repos.userGeneralData.save(data.apply { propertyValue = sb })
    }

    companion object {
        val logger = LoggerFactory.getLogger(UpsertUserAllHandler::class.java)
        const val SUCCESS = """{"returnCode":1,"apiName":"com.sega.maimai2servlet.api.UpsertUserAllApi"}"""
    }
}
