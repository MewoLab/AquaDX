package icu.samnyan.aqua.sega.maimai2.model.request.data;

import icu.samnyan.aqua.sega.maimai2.model.response.data.Mai2UserActivity;
import icu.samnyan.aqua.sega.maimai2.model.response.data.UserRating;
import icu.samnyan.aqua.sega.maimai2.model.userdata.*;
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
public class UserAll implements Serializable {
	private List<Mai2UserDetail> userData;
	private List<Mai2UserExtend> userExtend;
	private List<Mai2UserOption> userOption;
	private List<Mai2UserCharacter> userCharacterList;
	private List<Mai2UserGhost> userGhost;
	private List<Mai2UserMap> userMapList;
	private List<Mai2UserLoginBonus> userLoginBonusList;
	private List<UserRating> userRatingList;
	private List<Mai2UserItem> userItemList;
	private List<Mai2UserMusicDetail> userMusicDetailList;
	private List<Mai2UserCourse> userCourseList;
	private List<Mai2UserFriendSeasonRanking> userFriendSeasonRankingList;
	private List<Mai2UserCharge> userChargeList;
	private List<Mai2UserFavorite> userFavoriteList;
	private List<Mai2UserActivity> userActivityList;
	private List<Map<String, Object>> userGamePlaylogList;
	private List<UserFavoriteItem> userFavoritemusicList;
	private List<Mai2UserKaleidx> userKaleidxScopeList;
	private String isNewCharacterList;
	private String isNewMapList;
	private String isNewLoginBonusList;
	private String isNewItemList;
	private String isNewMusicDetailList;
	private String isNewCourseList;
	private String isNewFavoriteList;
	private String isNewFriendSeasonRankingList;
	private String isNewFavoritemusicList;
	private String isNewKaleidxScopeList;
}
