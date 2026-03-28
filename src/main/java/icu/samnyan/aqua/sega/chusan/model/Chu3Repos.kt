@file:Suppress("FunctionName")

package icu.samnyan.aqua.sega.chusan.model

import icu.samnyan.aqua.net.games.GenericPlaylogRepo
import icu.samnyan.aqua.net.games.GenericUserDataRepo
import icu.samnyan.aqua.net.games.GenericUserMusicRepo
import icu.samnyan.aqua.net.games.IUserRepo
import icu.samnyan.aqua.sega.chusan.model.userdata.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.stereotype.Component
import icu.samnyan.aqua.sega.util.GameDataService
import icu.samnyan.aqua.sega.util.StaticRepo


@NoRepositoryBean
interface Chu3UserLinked<T> : IUserRepo<Chu3UserData, T> {
    fun findByUser_Card_ExtId(extId: Long): List<T>
    fun findSingleByUser_Card_ExtId(extId: Long): T?
    fun findByUser_Card_ExtId(extId: Long, pageable: Pageable): Page<T>
}


// This repo cannot be generalized as UserLinked because the entity stores user as an int
// TODO: Find a way to generalize this
interface Chu3UserLoginBonusRepo : JpaRepository<UserLoginBonus, Long> {
    @Query(
        value = "select * from chusan_user_login_bonus where user = ?1 and version = ?2 and is_finished = ?3 order by last_update_date desc",
        nativeQuery = true
    )
    fun findAllLoginBonus(userId: Int, version: Int, isFinished: Int): List<UserLoginBonus>

    @Query(
        value = "select * from chusan_user_login_bonus where user = ?1 and version = ?2 and preset_id = ?3 limit 1",
        nativeQuery = true
    )
    fun findLoginBonus(userId: Int, version: Int, presetId: Long): UserLoginBonus?
}

interface Chu3UserActivityRepo : Chu3UserLinked<Chu3UserActivity> {
    fun findTopByUserAndActivityIdAndKindOrderByIdDesc(user: Chu3UserData, activityId: Int, kind: Int): Chu3UserActivity?
    fun findByUserAndActivityIdAndKind(user: Chu3UserData, activityId: Int, kind: Int): Chu3UserActivity?

    fun findAllByUser_Card_ExtIdAndKind(extId: Long, kind: Int): List<Chu3UserActivity>
}

interface Chu3UserCardPrintStateRepo : Chu3UserLinked<UserCardPrintState> {
    fun findByUser_Card_ExtIdAndHasCompleted(extId: Long, hasCompleted: Boolean): List<UserCardPrintState>

    fun findByUserAndGachaIdAndHasCompleted(userData: Chu3UserData, gachaId: Int, hasCompleted: Boolean): List<UserCardPrintState>
}

interface Chu3UserCharacterRepo : Chu3UserLinked<UserCharacter> {
    fun findTopByUserAndCharacterIdOrderByIdDesc(user: Chu3UserData, characterId: Int): UserCharacter?
    fun findByUserAndCharacterId(user: Chu3UserData, characterId: Int): UserCharacter?
}

interface Chu3UserChargeRepo : Chu3UserLinked<UserCharge> {
    fun findByUserAndChargeId(extId: Chu3UserData, chargeId: Int): UserCharge?
    fun findByUser_Card_ExtIdAndChargeId(ext: Long, chargeId: Int): UserCharge?
}

interface Chu3UserCourseRepo : Chu3UserLinked<UserCourse> {
    fun findTopByUserAndCourseIdOrderByIdDesc(user: Chu3UserData, courseId: Int): UserCourse?
    fun findByUserAndCourseId(user: Chu3UserData, courseId: Int): UserCourse?
}

interface Chu3UserLinkedVerseRepo : Chu3UserLinked<Chu3UserLinkedVerse> {
    fun findAllByUser(user: Chu3UserData): List<Chu3UserLinkedVerse?>
    fun findByUserAndLinkedVerseId(user: Chu3UserData, linkedVerseId: Int): Chu3UserLinkedVerse?
}

interface Chu3UserDataRepo : GenericUserDataRepo<Chu3UserData> {
    fun findTopByLastClientIdOrderByLastPlayDateDesc(lastClientId: String): Chu3UserData?
}

interface Chu3UserDuelRepo : Chu3UserLinked<UserDuel> {
    fun findTopByUserAndDuelIdOrderByIdDesc(user: Chu3UserData, duelId: Int): UserDuel?
    fun findByUserAndDuelId(user: Chu3UserData, duelId: Int): UserDuel?
}

interface Chu3UserGachaRepo : Chu3UserLinked<UserGacha> {
    fun findByUserAndGachaId(extId: Chu3UserData, gachaId: Int): UserGacha?
}

interface Chu3UserGameOptionRepo : Chu3UserLinked<UserGameOption>

interface Chu3UserGeneralDataRepo : Chu3UserLinked<UserGeneralData> {
    fun findByUserAndPropertyKey(user: Chu3UserData, key: String): UserGeneralData?

    fun findByUser_Card_ExtIdAndPropertyKey(extId: Long, key: String): UserGeneralData?
}

interface Chu3UserItemRepo : Chu3UserLinked<Chu3UserItem> {
    fun findAllByUser(user: Chu3UserData): List<Chu3UserItem>
    fun findTopByUserAndItemIdAndItemKindOrderByIdDesc(user: Chu3UserData, itemId: Int, itemKind: Int): Chu3UserItem?
    fun findByUserAndItemIdAndItemKind(user: Chu3UserData, itemId: Int, itemKind: Int): Chu3UserItem?

    fun findAllByUser_Card_ExtIdAndItemKind(extId: Long, itemKind: Int, pageable: Pageable): Page<Chu3UserItem>

    fun findAllByUser_Card_ExtIdAndItemKind(extId: Long, itemKind: Int): List<Chu3UserItem>
}

interface Chu3UserMapRepo : Chu3UserLinked<UserMap> {
    fun findByUserAndMapAreaId(user: Chu3UserData, mapAreaId: Int): UserMap?

    fun findAllByUserCardExtIdAndMapAreaIdIn(user: Long, mapAreaIds: List<Int>): List<UserMap>
}

interface Chu3UserMusicDetailRepo : Chu3UserLinked<UserMusicDetail>, GenericUserMusicRepo<UserMusicDetail> {
    fun findTopByUserAndMusicIdAndLevelOrderByIdDesc(user: Chu3UserData, musicId: Int, level: Int): UserMusicDetail?
    fun findByUserAndMusicIdAndLevel(user: Chu3UserData, musicId: Int, level: Int): UserMusicDetail?

    fun findByUser_Card_ExtIdAndMusicId(extId: Long, musicId: Int): List<UserMusicDetail>
}

interface Chu3UserPlaylogRepo : GenericPlaylogRepo<UserPlaylog>, Chu3UserLinked<UserPlaylog> {
    fun findByUser_Card_ExtIdAndLevelNot(extId: Long, levelNot: Int, page: Pageable): List<UserPlaylog>

    fun findByUser_Card_ExtIdAndMusicIdAndLevel(extId: Long, musicId: Int, level: Int): List<UserPlaylog>
}

interface Chu3UserCMissionRepo : Chu3UserLinked<UserCMission> {
    fun findByUser_Card_ExtIdAndMissionId(extId: Long, missionId: Int): UserCMission?
    fun findByUserAndMissionIdIn(user: Chu3UserData, missionIds: Collection<Int>): List<UserCMission>
}

interface Chu3UserCMissionProgressRepo : Chu3UserLinked<UserCMissionProgress> {
    fun findByUser_Card_ExtIdAndMissionId(extId: Long, missionId: Int): List<UserCMissionProgress>
    fun findByUserAndMissionId(user: Chu3UserData, missionId: Int): List<UserCMissionProgress>

    fun findByUser_Card_ExtIdAndMissionIdAndOrder(extId: Long, missionId: Int, order: Int): UserCMissionProgress?
}

interface Chu3NetBattleLogRepo : Chu3UserLinked<Chu3NetBattleLog> {
    fun findTop20ByUserOrderByIdDesc(user: Chu3UserData): List<Chu3NetBattleLog>
}

interface Chu3UserMiscRepo : Chu3UserLinked<Chu3UserMisc>

interface Chu3UserChallengeRepo : Chu3UserLinked<Chu3UserChallenge> {
    fun findByUserAndUnlockChallengeId(user: Chu3UserData, unlockChallengeId: Int): Chu3UserChallenge?
}

interface Chu3UserRegionsRepo: Chu3UserLinked<UserRegions> {
    fun findByUserAndRegionId(user: Chu3UserData, regionId: Int): UserRegions?
}

class Chu3GameGachaCardRepo(data: List<GameGachaCard>) : StaticRepo<GameGachaCard, Long>(data, { it.id }) {
    fun findAllByGachaId(gachaId: Int) = data.filter { it.gachaId == gachaId }
}

class Chu3GameLoginBonusPresetsRepo(data: List<GameLoginBonusPreset>) : StaticRepo<GameLoginBonusPreset, Long>(data, { it.id }) {
    fun findLoginBonusPresets(version: Int, isEnabled: Int) = data.filter { it.version == version && it.isEnabled == (isEnabled == 1) }
}

class Chu3GameLoginBonusRepo(data: List<GameLoginBonus>) : StaticRepo<GameLoginBonus, Long>(data, { it.id }) {
    fun findGameLoginBonus(version: Int, presetId: Int) = data.filter { it.version == version && it.presetId == presetId }.sortedByDescending { it.needLoginDayCount }
    fun findByRequiredDays(version: Int, presetId: Int, requiredDays: Int) = data.find { it.version == version && it.presetId == presetId && it.needLoginDayCount == requiredDays }
}

@Component
class Chu3Repos(
    val userLoginBonus: Chu3UserLoginBonusRepo,
    val userActivity: Chu3UserActivityRepo,
    val userCardPrintState: Chu3UserCardPrintStateRepo,
    val userCharacter: Chu3UserCharacterRepo,
    val userCharge: Chu3UserChargeRepo,
    val userCourse: Chu3UserCourseRepo,
    val userData: Chu3UserDataRepo,
    val userDuel: Chu3UserDuelRepo,
    val userGacha: Chu3UserGachaRepo,
    val userGameOption: Chu3UserGameOptionRepo,
    val userGeneralData: Chu3UserGeneralDataRepo,
    val userItem: Chu3UserItemRepo,
    val userMap: Chu3UserMapRepo,
    val userMusicDetail: Chu3UserMusicDetailRepo,
    val userPlaylog: Chu3UserPlaylogRepo,
    val userRegions: Chu3UserRegionsRepo,
    val userCMission: Chu3UserCMissionRepo,
    val userCMissionProgress: Chu3UserCMissionProgressRepo,
    val netBattleLog: Chu3NetBattleLogRepo,
    val userMisc: Chu3UserMiscRepo,
    val userChallenge: Chu3UserChallengeRepo,
    val userLinkedVerse: Chu3UserLinkedVerseRepo,
    val gameData: GameDataService
) {
    val gameCharge = StaticRepo(gameData.chu3GameCharges) { it.orderId.toLong() }
    val gameEvent = StaticRepo(gameData.chu3GameEvents) { it.id }
    val gameGachaCard = Chu3GameGachaCardRepo(gameData.chu3GameGachaCards)
    val gameGacha = StaticRepo(gameData.chu3GameGachas) { it.gachaId.toLong() }
    val gameLoginBonusPresets = Chu3GameLoginBonusPresetsRepo(gameData.chu3GameLoginBonusPresets)
    val gameLoginBonus = Chu3GameLoginBonusRepo(gameData.chu3GameLoginBonuses)
    val gameLinkedVerse = StaticRepo(gameData.chu3GameLinkedVerses) { it.id }
}
