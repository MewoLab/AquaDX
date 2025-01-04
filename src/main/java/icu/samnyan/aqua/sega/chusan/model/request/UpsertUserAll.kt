package icu.samnyan.aqua.sega.chusan.model.request

import ext.JDict
import icu.samnyan.aqua.sega.chusan.model.userdata.*
import icu.samnyan.aqua.sega.general.model.response.UserRecentRating

class UserCMissionResp {
    var missionId = 0
    var point = 0
    var userCMissionProgressList: List<UserCMissionProgress>? = null
}

class FavNewMusic(
    var musicId: Int = 0,
    var orderId: Int = 0,
)

class UpsertTeamPoint(
    // userId exists here, but it should not be used
    // So I will not include it in the data class
    var teamId: Long = 0,
    var orderId: Int = 0,
    var teamPoint: Long = 0,
    var aggrDate: String = "",
)

data class UpsertNetBattleData(
    val recentNBSelectMusicList: List<UpsertNBSelectMusic> = emptyList(),
    val isRankUpChallengeFailed: Boolean = false,
    val highestBattleRankId: Long = 0,
    val battleIconId: Long = 0,
    val battleIconNum: Long = 0,
    val avatarEffectPoint: Long = 0,
)

data class UpsertNBSelectMusic(
    val musicId: Int = 0,
)

class UpsertUserAll(
    var userData: List<Chu3UserData>? = null,
    var userGameOption: List<UserGameOption>? = null,
    var userCharacterList: List<UserCharacter>? = null,
    var userItemList: List<UserItem>? = null,
    var userMusicDetailList: List<UserMusicDetail>? = null,
    var userActivityList: List<UserActivity>? = null,
    var userRecentRatingList: List<UserRecentRating>? = null,
    var userPlaylogList: List<UserPlaylog>? = null,
    var userChargeList: List<UserCharge>? = null,
    var userCourseList: List<UserCourse>? = null,
    var userDuelList: List<UserDuel>? = null,
    // TODO: Actually implement team
    var userTeamPoint: List<UpsertTeamPoint>? = null,
    var userRatingBaseHotList: List<UserRecentRating>? = null,
    var userRatingBaseList: List<UserRecentRating>? = null,
    var userRatingBaseNextList: List<UserRecentRating>? = null,
    var userLoginBonusList: List<JDict>? = null,
    var userMapAreaList: List<UserMap>? = null,
    var userOverPowerList: List<JDict>? = null,
    var userNetBattlelogList: List<Chu3NetBattleLog>? = null,
    var userEmoneyList: List<JDict>? = null,
    var userNetBattleData: List<UpsertNetBattleData>? = null,
    var userCMissionList: List<UserCMissionResp>? = null,
    var userFavoriteMusicList: List<FavNewMusic>? = null,
)
