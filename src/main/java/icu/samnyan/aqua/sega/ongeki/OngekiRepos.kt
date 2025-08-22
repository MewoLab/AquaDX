@file:Suppress("FunctionName")
package icu.samnyan.aqua.sega.ongeki

import icu.samnyan.aqua.net.games.GenericPlaylogRepo
import icu.samnyan.aqua.net.games.GenericUserDataRepo
import icu.samnyan.aqua.net.games.GenericUserMusicRepo
import icu.samnyan.aqua.net.games.IUserRepo
import icu.samnyan.aqua.sega.ongeki.model.*
import icu.samnyan.aqua.sega.ongeki.model.UserEventMap
import icu.samnyan.aqua.sega.ongeki.model.UserSkin
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.stereotype.Component
import java.util.*


@NoRepositoryBean
interface OngekiUserLinked<T> : IUserRepo<UserData, T> {
    fun findByUser_Card_ExtId(extId: Long): List<T>
    fun findSingleByUser_Card_ExtId(extId: Long): Optional<T>
    fun findByUser_Card_ExtId(extId: Long, pageable: Pageable): Page<T>
}

interface OgkUserDataRepo : GenericUserDataRepo<UserData> {
    fun findByCard_ExtIdIn(userIds: Collection<Long>): List<UserData>
}

interface OgkUserActivityRepo : OngekiUserLinked<UserActivity> {
    fun findByUserAndKindAndActivityId(userData: UserData, kind: Int, activityId: Int): Optional<UserActivity>
    fun findByUser_Card_ExtIdAndKindOrderBySortNumberDesc(userId: Long, kind: Int): List<UserActivity>
}

interface OgkUserBossRepo : OngekiUserLinked<UserBoss> {
    fun findByUserAndMusicId(user: UserData, musicId: Int): Optional<UserBoss>
}

interface OgkUserCardRepo : OngekiUserLinked<UserCard> {
    fun findByUserAndCardId(userData: UserData, cardId: Int): Optional<UserCard>
}

interface OgkUserChapterRepo : OngekiUserLinked<UserChapter> {
    fun findByUserAndChapterId(userData: UserData, chapterId: Int): Optional<UserChapter>
}

interface OgkUserCharacterRepo : OngekiUserLinked<UserCharacter> {
    fun findByUserAndCharacterId(userData: UserData, characterId: Int): Optional<UserCharacter>
}

interface OgkUserDeckRepo : OngekiUserLinked<UserDeck> {
    fun findByUserAndDeckId(userData: UserData, deckId: Int): Optional<UserDeck>
}

interface OgkUserEventMusicRepo : OngekiUserLinked<UserEventMusic> {
    fun findByUserAndEventIdAndTypeAndMusicId(
        userData: UserData,
        eventId: Int,
        type: Int,
        musicId: Int
    ): Optional<UserEventMusic>
}

interface OgkUserEventPointRepo : OngekiUserLinked<UserEventPoint> {
    fun findByUserAndEventId(userData: UserData, eventId: Int): Optional<UserEventPoint>

    //@Query(value = "SELECT rank from (SELECT user_id , DENSE_RANK() OVER (ORDER BY point DESC) as rank from ongeki_user_event_point where event_id = :eventId) where user_id == :userId limit 1", nativeQuery = true)
    @Query("SELECT COUNT(u)+1 FROM OngekiUserEventPoint u WHERE u.eventId = :eventId AND u.point > (SELECT u2.point FROM OngekiUserEventPoint u2 WHERE u2.user.id = :userId AND u2.eventId = :eventId)")
    fun calculateRankByUserAndEventId(userId: Long, eventId: Int): Int
}

interface OgkUserGeneralDataRepo : OngekiUserLinked<UserGeneralData> {
    fun findByUserAndPropertyKey(user: UserData, key: String): Optional<UserGeneralData>

    fun findByUser_Card_ExtIdAndPropertyKey(userId: Long, key: String): Optional<UserGeneralData>
}

interface OgkUserItemRepo : OngekiUserLinked<UserItem> {
    fun findByUserAndItemKindAndItemId(userData: UserData, itemKind: Int, itemId: Int): Optional<UserItem>

    fun findByUser_Card_ExtIdAndItemKind(userId: Long, kind: Int, page: Pageable): Page<UserItem>
    fun findByUser_Card_ExtIdAndItemKind(userId: Long, kind: Int): List<UserItem>
}

interface OgkUserKopRepo : OngekiUserLinked<UserKop> {
    fun findByUserAndKopIdAndAreaId(userData: UserData, kopId: Int, areaId: Int): Optional<UserKop>
}

interface OgkUserLoginBonusRepo : OngekiUserLinked<UserLoginBonus> {
    fun findByUserAndBonusId(userData: UserData, bonusId: Int): Optional<UserLoginBonus>
}

interface OgkUserMemoryChapterRepo : OngekiUserLinked<UserMemoryChapter> {
    fun findByUserAndChapterId(userData: UserData, chapterId: Int): Optional<UserMemoryChapter>
}

interface OgkUserMissionPointRepo : OngekiUserLinked<UserMissionPoint> {
    fun findByUserAndEventId(userData: UserData, eventId: Int): Optional<UserMissionPoint>
}

interface OgkUserMusicDetailRepo : OngekiUserLinked<UserMusicDetail>, GenericUserMusicRepo<UserMusicDetail> {
    fun findByUserAndMusicIdAndLevel(userData: UserData, musicId: Int, level: Int): Optional<UserMusicDetail>
}

interface OgkUserMusicItemRepo : OngekiUserLinked<UserMusicItem> {
    fun findByUserAndMusicId(userData: UserData, musicId: Int): Optional<UserMusicItem>
}

interface OgkUserOptionRepo : OngekiUserLinked<UserOption>

interface OgkUserPlaylogRepo : OngekiUserLinked<UserPlaylog>, GenericPlaylogRepo<UserPlaylog>

interface OgkUserRivalDataRepo : OngekiUserLinked<UserRival>

interface OgkUserScenarioRepo : OngekiUserLinked<UserScenario> {
    fun findByUserAndScenarioId(user: UserData, scenarioId: Int): Optional<UserScenario>
}

interface OgkUserStoryRepo : OngekiUserLinked<UserStory> {
    fun findByUserAndStoryId(userData: UserData, storyId: Int): Optional<UserStory>
}

interface OgkUserTechCountRepo : OngekiUserLinked<UserTechCount> {
    fun findByUserAndLevelId(user: UserData, levelId: Int): Optional<UserTechCount>
}

interface OgkUserTechEventRepo : OngekiUserLinked<UserTechEvent> {
    fun findByUserAndEventId(userData: UserData, eventId: Int): Optional<UserTechEvent>
}

interface OgkUserTradeItemRepo : OngekiUserLinked<UserTradeItem> {
    fun findByUser_Card_ExtIdAndChapterIdGreaterThanEqualAndChapterIdLessThanEqual(
        userId: Long,
        startChapterId: Int,
        endChapterId: Int
    ): List<UserTradeItem>

    fun findByUserAndChapterIdAndTradeItemId(
        userData: UserData,
        chapterId: Int,
        tradeItemId: Int
    ): Optional<UserTradeItem>
}

interface OgkUserTrainingRoomRepo : OngekiUserLinked<UserTrainingRoom> {
    fun findByUserAndRoomId(user: UserData, roomId: Int): Optional<UserTrainingRoom>
}

// Re:Fresh
interface OgkUserEventMapRepo : OngekiUserLinked<UserEventMap>
interface OgkUserSkinRepo : OngekiUserLinked<UserSkin>

interface OgkGameCardRepo : JpaRepository<GameCard, Long>
interface OgkGameCharaRepo : JpaRepository<GameChara, Long>
interface OgkGameEventRepo : JpaRepository<GameEvent, Long>
interface OgkGameMusicRepo : JpaRepository<GameMusic, Long>
interface OgkGamePointRepo : JpaRepository<GamePoint, Long>
interface OgkGamePresentRepo : JpaRepository<GamePresent, Long>
interface OgkGameRewardRepo : JpaRepository<GameReward, Long>
interface OgkGameSkillRepo : JpaRepository<GameSkill, Long>

@Component
class OngekiUserRepos(
    val data: OgkUserDataRepo,
    val activity: OgkUserActivityRepo,
    val boss: OgkUserBossRepo,
    val card: OgkUserCardRepo,
    val chapter: OgkUserChapterRepo,
    val character: OgkUserCharacterRepo,
    val deck: OgkUserDeckRepo,
    val eventMusic: OgkUserEventMusicRepo,
    val eventPoint: OgkUserEventPointRepo,
    val generalData: OgkUserGeneralDataRepo,
    val item: OgkUserItemRepo,
    val kop: OgkUserKopRepo,
    val loginBonus: OgkUserLoginBonusRepo,
    val memoryChapter: OgkUserMemoryChapterRepo,
    val missionPoint: OgkUserMissionPointRepo,
    val musicDetail: OgkUserMusicDetailRepo,
    val musicItem: OgkUserMusicItemRepo,
    val option: OgkUserOptionRepo,
    val playlog: OgkUserPlaylogRepo,
    val rivalData: OgkUserRivalDataRepo,
    val scenario: OgkUserScenarioRepo,
    val story: OgkUserStoryRepo,
    val techCount: OgkUserTechCountRepo,
    val techEvent: OgkUserTechEventRepo,
    val tradeItem: OgkUserTradeItemRepo,
    val trainingRoom: OgkUserTrainingRoomRepo,
    val eventMap: OgkUserEventMapRepo,
    val skin: OgkUserSkinRepo,
)

@Component
class OngekiGameRepos(
    val card: OgkGameCardRepo,
    val chara: OgkGameCharaRepo,
    val event: OgkGameEventRepo,
    val music: OgkGameMusicRepo,
    val point: OgkGamePointRepo,
    val present: OgkGamePresentRepo,
    val reward: OgkGameRewardRepo,
    val skill: OgkGameSkillRepo,
)

@Component
class OngekiRepos(
    val u: OngekiUserRepos,
    val g: OngekiGameRepos,
)

