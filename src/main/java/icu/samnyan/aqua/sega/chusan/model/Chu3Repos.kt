@file:Suppress("FunctionName")

package icu.samnyan.aqua.sega.chusan.model

import icu.samnyan.aqua.net.games.GenericPlaylogRepo
import icu.samnyan.aqua.net.games.GenericUserDataRepo
import icu.samnyan.aqua.net.games.IUserRepo
import icu.samnyan.aqua.sega.chusan.model.gamedata.*
import icu.samnyan.aqua.sega.chusan.model.userdata.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.stereotype.Component
import java.util.*


@NoRepositoryBean
interface Chu3UserLinked<T> : IUserRepo<Chu3UserData, T> {
    fun findByUser_Card_ExtId(extId: Long): List<T>
    fun findSingleByUser_Card_ExtId(extId: Long): Optional<T>
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
    fun findLoginBonus(userId: Int, version: Int, presetId: Int): Optional<UserLoginBonus>
}

interface Chu3UserActivityRepo : Chu3UserLinked<UserActivity> {
    fun findTopByUserAndActivityIdAndKindOrderByIdDesc(user: Chu3UserData, activityId: Int, kind: Int): Optional<UserActivity>
    fun findByUserAndActivityIdAndKind(user: Chu3UserData, activityId: Int, kind: Int): UserActivity?

    fun findAllByUser_Card_ExtIdAndKind(extId: Long, kind: Int): List<UserActivity>
}

interface Chu3UserCardPrintStateRepo : Chu3UserLinked<UserCardPrintState> {
    fun findByUser_Card_ExtIdAndHasCompleted(extId: Long, hasCompleted: Boolean): List<UserCardPrintState>

    fun findByUserAndGachaIdAndHasCompleted(userData: Chu3UserData, gachaId: Int, hasCompleted: Boolean): List<UserCardPrintState>
}

interface Chu3UserCharacterRepo : Chu3UserLinked<UserCharacter> {
    fun findTopByUserAndCharacterIdOrderByIdDesc(user: Chu3UserData, characterId: Int): Optional<UserCharacter>
    fun findByUserAndCharacterId(user: Chu3UserData, characterId: Int): UserCharacter?
}

interface Chu3UserChargeRepo : Chu3UserLinked<UserCharge> {
    fun findByUserAndChargeId(extId: Chu3UserData, chargeId: Int): Optional<UserCharge>
    fun findByUser_Card_ExtIdAndChargeId(ext: Long, chargeId: Int): UserCharge?
}

interface Chu3UserCourseRepo : Chu3UserLinked<UserCourse> {
    fun findTopByUserAndCourseIdOrderByIdDesc(user: Chu3UserData, courseId: Int): Optional<UserCourse>
    fun findByUserAndCourseId(user: Chu3UserData, courseId: Int): UserCourse?
}

interface Chu3UserDataRepo : GenericUserDataRepo<Chu3UserData> {
    fun findTopByLastClientIdOrderByLastPlayDateDesc(lastClientId: String): Chu3UserData?
}

interface Chu3UserDuelRepo : Chu3UserLinked<UserDuel> {
    fun findTopByUserAndDuelIdOrderByIdDesc(user: Chu3UserData, duelId: Int): Optional<UserDuel>
    fun findByUserAndDuelId(user: Chu3UserData, duelId: Int): UserDuel?
}

interface Chu3UserGachaRepo : Chu3UserLinked<UserGacha> {
    fun findByUserAndGachaId(extId: Chu3UserData, gachaId: Int): Optional<UserGacha>
}

interface Chu3UserGameOptionRepo : Chu3UserLinked<UserGameOption>

interface Chu3UserGeneralDataRepo : Chu3UserLinked<UserGeneralData> {
    fun findByUserAndPropertyKey(user: Chu3UserData, key: String): Optional<UserGeneralData>

    fun findByUser_Card_ExtIdAndPropertyKey(extId: Long, key: String): Optional<UserGeneralData>
}

interface Chu3UserItemRepo : Chu3UserLinked<UserItem> {
    fun findAllByUser(user: Chu3UserData): List<UserItem>
    fun findTopByUserAndItemIdAndItemKindOrderByIdDesc(user: Chu3UserData, itemId: Int, itemKind: Int): Optional<UserItem>
    fun findByUserAndItemIdAndItemKind(user: Chu3UserData, itemId: Int, itemKind: Int): UserItem?

    fun findAllByUser_Card_ExtIdAndItemKind(extId: Long, itemKind: Int, pageable: Pageable): Page<UserItem>

    fun findAllByUser_Card_ExtIdAndItemKind(extId: Long, itemKind: Int): List<UserItem>
}

interface Chu3UserMapRepo : Chu3UserLinked<UserMap> {
    fun findByUserAndMapAreaId(user: Chu3UserData, mapAreaId: Int): UserMap?

    fun findAllByUserCardExtIdAndMapAreaIdIn(user: Long, mapAreaIds: List<Int>): List<UserMap>
}

interface Chu3UserMusicDetailRepo : Chu3UserLinked<UserMusicDetail> {
    fun findTopByUserAndMusicIdAndLevelOrderByIdDesc(user: Chu3UserData, musicId: Int, level: Int): Optional<UserMusicDetail>
    fun findByUserAndMusicIdAndLevel(user: Chu3UserData, musicId: Int, level: Int): UserMusicDetail?

    fun findByUser_Card_ExtIdAndMusicId(extId: Long, musicId: Int): List<UserMusicDetail>
}

interface Chu3UserPlaylogRepo : GenericPlaylogRepo<UserPlaylog>, Chu3UserLinked<UserPlaylog> {
    fun findByUser_Card_ExtIdAndLevelNot(extId: Long, levelNot: Int, page: Pageable): List<UserPlaylog>

    fun findByUser_Card_ExtIdAndMusicIdAndLevel(extId: Long, musicId: Int, level: Int): List<UserPlaylog>
}

interface Chu3UserCMissionRepo : Chu3UserLinked<UserCMission> {
    fun findByUser_Card_ExtIdAndMissionId(extId: Long, missionId: Int): Optional<UserCMission>
}

interface Chu3UserCMissionProgressRepo : Chu3UserLinked<UserCMissionProgress> {
    fun findByUser_Card_ExtIdAndMissionId(extId: Long, missionId: Int): List<UserCMissionProgress>

    fun findByUser_Card_ExtIdAndMissionIdAndOrder(extId: Long, missionId: Int, order: Int): Optional<UserCMissionProgress>
}

interface Chu3MatchingMemberRepo : JpaRepository<Chu3MatchingMember, Long> {
    fun existsByUserIdAndUserName(userId: Long, userName: String): Boolean
}

interface Chu3GameChargeRepo : JpaRepository<GameCharge, Long>

interface Chu3GameEventRepo : JpaRepository<GameEvent, Int> {
    fun findByEnable(enable: Boolean): List<GameEvent>
}

interface Chu3GameGachaCardRepo : JpaRepository<GameGachaCard, Long> {
    fun findAllByGachaId(gachaId: Int): List<GameGachaCard>
}

interface Chu3GameGachaRepo : JpaRepository<GameGacha, Long>

interface Chu3GameLoginBonusPresetsRepo : JpaRepository<GameLoginBonusPreset, Int> {
    @Query(
        value = "select * from chusan_game_login_bonus_preset where version = ?1 and is_enabled = ?2",
        nativeQuery = true
    )
    fun findLoginBonusPresets(version: Int, isEnabled: Int): List<GameLoginBonusPreset>
}

interface Chu3GameLoginBonusRepo : JpaRepository<GameLoginBonus, Int> {
    @Query(
        value = "select * from chusan_game_login_bonus where version = ?1 and preset_id = ?2 order by need_login_day_count desc",
        nativeQuery = true
    )
    fun findGameLoginBonus(version: Int, presetId: Int): List<GameLoginBonus>

    @Query(
        value = "select * from chusan_game_login_bonus where version = ?1 and preset_id = ?2 and need_login_day_count = ?3 limit 1",
        nativeQuery = true
    )
    fun findByRequiredDays(version: Int, presetId: Int, requiredDays: Int): Optional<GameLoginBonus>
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
    val userCMission: Chu3UserCMissionRepo,
    val userCMissionProgress: Chu3UserCMissionProgressRepo,
    val matchingMember: Chu3MatchingMemberRepo,
    val gameCharge: Chu3GameChargeRepo,
    val gameEvent: Chu3GameEventRepo,
    val gameGachaCard: Chu3GameGachaCardRepo,
    val gameGacha: Chu3GameGachaRepo,
    val gameLoginBonusPresets: Chu3GameLoginBonusPresetsRepo,
    val gameLoginBonus: Chu3GameLoginBonusRepo
)