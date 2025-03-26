package icu.samnyan.aqua.sega.ongeki.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import icu.samnyan.aqua.sega.general.model.response.UserRecentRating;
import icu.samnyan.aqua.sega.ongeki.model.gamedata.OngekiFumenScore;
import icu.samnyan.aqua.sega.ongeki.model.userdata.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpsertUserAll implements Serializable {

    private List<UserData> userData;

    private List<UserOption> userOption;

    private List<UserPlaylog> userPlaylogList;

    private List<Map<String, Object>> userJewelboostlogList;

    private List<Map<String, Object>> userSessionlogList;

    private List<UserActivity> userActivityList;

    private List<UserRecentRating> userRecentRatingList;

    private List<UserRecentRating> userBpBaseList;

    private List<UserRecentRating> userRatingBaseBestNewList;

    private List<UserRecentRating> userRatingBaseBestList;

    private List<UserRecentRating> userRatingBaseHotList;

    private List<UserRecentRating> userRatingBaseNextNewList;

    private List<UserRecentRating> userRatingBaseNextList;

    private List<UserRecentRating> userRatingBaseHotNextList;

    private List<OngekiFumenScore> userNewRatingBasePScoreList;

    private List<OngekiFumenScore> userNewRatingBaseBestList;

    private List<OngekiFumenScore> userNewRatingBaseBestNewList;

    private List<OngekiFumenScore> userNewRatingBaseNextPScoreList;

    private List<OngekiFumenScore> userNewRatingBaseNextBestList;

    private List<OngekiFumenScore> userNewRatingBaseNextBestNewList;

    private List<UserMusicDetail> userMusicDetailList;

    private List<UserCharacter> userCharacterList;

    private List<UserCard> userCardList;

    private List<UserDeck> userDeckList;

    private List<UserTrainingRoom> userTrainingRoomList;

    private List<UserStory> userStoryList;

    private List<UserChapter> userChapterList;

    private List<UserMemoryChapter> userMemoryChapterList;

    private List<UserItem> userItemList;

    private List<UserMusicItem> userMusicItemList;

    private List<UserLoginBonus> userLoginBonusList;

    private List<UserEventPoint> userEventPointList;

    private List<UserMissionPoint> userMissionPointList;

    private List<Map<String, Object>> userRatinglogList;

    private List<UserBoss> userBossList;

    private List<UserTechCount> userTechCountList;

    private List<UserScenario> userScenarioList;

    private List<UserTradeItem> userTradeItemList;

    private List<UserEventMusic> userEventMusicList;

    private List<UserTechEvent> userTechEventList;

    private List<UserKop> userKopList;

    public UserEventMap userEventMap;

    private Map<String, Object> clientSystemInfo;

    @JsonProperty("isNewMusicDetailList")
    private String isNewMusicDetailList;

    @JsonProperty("isNewCharacterList")
    private String isNewCharacterList;

    @JsonProperty("isNewCardList")
    private String isNewCardList;

    @JsonProperty("isNewDeckList")
    private String isNewDeckList;

    @JsonProperty("isNewTrainingRoomList")
    private String isNewTrainingRoomList;

    @JsonProperty("isNewStoryList")
    private String isNewStoryList;

    @JsonProperty("isNewChapterList")
    private String isNewChapterList;

    @JsonProperty("isNewMemoryChapterList")
    private String isNewMemoryChapterList;

    @JsonProperty("isNewItemList")
    private String isNewItemList;

    @JsonProperty("isNewMusicItemList")
    private String isNewMusicItemList;

    @JsonProperty("isNewLoginBonusList")
    private String isNewLoginBonusList;

    @JsonProperty("isNewEventPointList")
    private String isNewEventPointList;

    @JsonProperty("isNewMissionPointList")
    private String isNewMissionPointList;

    @JsonProperty("isNewRatinglogList")
    private String isNewRatinglogList;

    @JsonProperty("isNewBossList")
    private String isNewBossList;

    @JsonProperty("isNewTechCountList")
    private String isNewTechCountList;

    @JsonProperty("isNewScenarioList")
    private String isNewScenarioList;

    @JsonProperty("isNewTradeItemList")
    private String isNewTradeItemList;

    @JsonProperty("isNewEventMusicList")
    private String isNewEventMusicList;

    @JsonProperty("isNewTechEventList")
    private String isNewTechEventList;

    @JsonProperty("isNewKopList")
    private String isNewKopList;

}
