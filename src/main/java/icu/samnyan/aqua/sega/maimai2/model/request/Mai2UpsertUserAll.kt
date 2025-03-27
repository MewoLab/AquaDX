package icu.samnyan.aqua.sega.maimai2.model.request

import icu.samnyan.aqua.sega.maimai2.model.UserRating
import icu.samnyan.aqua.sega.maimai2.model.userdata.*

class Mai2UpsertUserAll(
    var userId: Long,
    var upsertUserAll: Mai2UserAll
)

class Mai2UserAll {
    var userData: List<Mai2UserDetail> = emptyList()
    var userOption: List<Mai2UserOption>? = null
    var userExtend: List<Mai2UserExtend>? = null
    var userCharacterList: List<Mai2UserCharacter>? = null
    var userGhost: List<Mai2UserGhost>? = null
    var userMapList: List<Mai2UserMap>? = null
    var userLoginBonusList: List<Mai2UserLoginBonus>? = null
    var userRatingList: List<UserRating>? = null
    var userItemList: List<Mai2UserItem>? = null
    var userMusicDetailList: List<Mai2UserMusicDetail>? = null
    var userCourseList: List<Mai2UserCourse>? = null
    var userFriendSeasonRankingList: List<Mai2UserFriendSeasonRanking>? = null
    var userChargeList: List<Mai2UserCharge>? = null
    var userFavoriteList: List<Mai2UserFavorite>? = null
    var userActivityList: List<Mai2UserActivity>? = null
    var userGamePlaylogList: List<Map<String, Any>>? = null
    var userFavoritemusicList: List<Mai2UserFavoriteItem>? = null
    var userKaleidxScopeList: List<Mai2UserKaleidx>? = null
    var userIntimateList: List<Mai2UserIntimate>? = null
    var isNewCharacterList: String? = null
    var isNewMapList: String? = null
    var isNewLoginBonusList: String? = null
    var isNewItemList: String? = null
    var isNewMusicDetailList: String? = null
    var isNewCourseList: String? = null
    var isNewFavoriteList: String? = null
    var isNewFriendSeasonRankingList: String? = null
    var isNewFavoritemusicList: String? = null
    var isNewKaleidxScopeList: String? = null
}

class Mai2UserFavoriteItem {
    var orderId = 0
    var id = 0
}

class Mai2UserActivity {
    var playList: List<Mai2UserAct> = emptyList()
    var musicList: List<Mai2UserAct> = emptyList()
}


