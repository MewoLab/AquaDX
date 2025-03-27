package icu.samnyan.aqua.api.model.resp.sega.chuni.v2.external;

import icu.samnyan.aqua.sega.chusan.model.userdata.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * This class is use for importing chusan profile
 * @author samnyan (privateamusement@protonmail.com)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChuniDataImport {
    private String gameId;
    private ExternalUserData userData;
    private List<Chu3UserActivity> userActivityList;
    private List<UserCharacter> userCharacterList;
    private List<UserCharge> userChargeList;
    private List<UserCourse> userCourseList;
    private List<UserDuel> userDuelList;
    private UserGameOption userGameOption;
    private List<Chu3UserItem> userItemList;
    private List<UserMap> userMapList;
    private List<UserMusicDetail> userMusicDetailList;
    private List<UserPlaylog> userPlaylogList;
}
