package icu.samnyan.aqua.sega.ongeki.model
import icu.samnyan.aqua.sega.general.model.response.UserRecentRating

class OngekiUpsertUserAll {
    var userData: List<UserData>? = null
    var userOption: List<UserOption>? = null
    var userPlaylogList: List<UserPlaylog>? = null
    var userJewelboostlogList: List<Map<String, Any>>? = null
    var userSessionlogList: List<Map<String, Any>>? = null
    var userActivityList: List<UserActivity>? = null

    var userRecentRatingList: List<UserRecentRating>? = null
    var userBpBaseList: List<UserRecentRating>? = null
    var userRatingBaseBestNewList: List<UserRecentRating>? = null
    var userRatingBaseBestList: List<UserRecentRating>? = null
    var userRatingBaseHotList: List<UserRecentRating>? = null
    var userRatingBaseNextNewList: List<UserRecentRating>? = null
    var userRatingBaseNextList: List<UserRecentRating>? = null
    var userRatingBaseHotNextList: List<UserRecentRating>? = null

    var userMusicDetailList: List<UserMusicDetail>? = null
    var userCharacterList: List<UserCharacter>? = null
    var userCardList: List<UserCard>? = null
    var userDeckList: List<UserDeck>? = null
    var userTrainingRoomList: List<UserTrainingRoom>? = null
    var userStoryList: List<UserStory>? = null
    var userChapterList: List<UserChapter>? = null
    var userMemoryChapterList: List<UserMemoryChapter>? = null
    var userItemList: List<UserItem>? = null
    var userMusicItemList: List<UserMusicItem>? = null
    var userLoginBonusList: List<UserLoginBonus>? = null
    var userEventPointList: List<UserEventPoint>? = null
    var userMissionPointList: List<UserMissionPoint>? = null
    var userRatinglogList: List<Map<String, Any>>? = null
    var userBossList: List<UserBoss>? = null
    var userTechCountList: List<UserTechCount>? = null
    var userScenarioList: List<UserScenario>? = null
    var userTradeItemList: List<UserTradeItem>? = null
    var userEventMusicList: List<UserEventMusic>? = null
    var userTechEventList: List<UserTechEvent>? = null
    var userKopList: List<UserKop>? = null
    var clientSystemInfo: Map<String, Any>? = null
    var isNewMusicDetailList: String? = null
    var isNewCharacterList: String? = null
    var isNewCardList: String? = null
    var isNewDeckList: String? = null
    var isNewTrainingRoomList: String? = null
    var isNewStoryList: String? = null
    var isNewChapterList: String? = null
    var isNewMemoryChapterList: String? = null
    var isNewItemList: String? = null
    var isNewMusicItemList: String? = null
    var isNewLoginBonusList: String? = null
    var isNewEventPointList: String? = null
    var isNewMissionPointList: String? = null
    var isNewRatinglogList: String? = null
    var isNewBossList: String? = null
    var isNewTechCountList: String? = null
    var isNewScenarioList: String? = null
    var isNewTradeItemList: String? = null
    var isNewEventMusicList: String? = null
    var isNewTechEventList: String? = null
    var isNewKopList: String? = null

    // Re:Fresh
    var userNewRatingBasePScoreList: List<OngekiFumenScore>? = null
    var userNewRatingBaseBestList: List<OngekiFumenScore>? = null
    var userNewRatingBaseBestNewList: List<OngekiFumenScore>? = null
    var userNewRatingBaseNextPScoreList: List<OngekiFumenScore>? = null
    var userNewRatingBaseNextBestList: List<OngekiFumenScore>? = null
    var userNewRatingBaseNextBestNewList: List<OngekiFumenScore>? = null
    var userEventMap: UserEventMap? = null
}

class OngekiFumenScore(
    var musicId: Int = 0,
    var difficultId: Int = 0,
    var romVersionCode: String? = null,
    var score: Int = 0,
    var platinumScoreMax: Int = 0,
    var platinumScoreStar: Int = 0,
) {
    override fun toString() = "${musicId}:${difficultId}:${score}:${platinumScoreStar}:${platinumScoreMax}"
}
