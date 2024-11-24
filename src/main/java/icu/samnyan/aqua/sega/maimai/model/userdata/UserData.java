package icu.samnyan.aqua.sega.maimai.model.userdata;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import icu.samnyan.aqua.sega.general.model.Card;
import icu.samnyan.aqua.sega.util.jackson.AccessCodeSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Entity(name = "MaimaiUserData")
@Table(name = "maimai_user_data")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserData implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private long id;

    @JsonSerialize(using = AccessCodeSerializer.class)
    @JsonProperty(value = "accessCode", access = JsonProperty.Access.READ_ONLY)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aime_card_id")
    private Card card;

    private int lastDataVersion;

    private String userName;

    private int point;

    private int totalPoint;

    private int iconId;

    private int nameplateId;

    private int frameId;

    private int trophyId;

    private int playCount;

    private int playVsCount;

    private int playSyncCount;

    private int winCount;

    private int helpCount;

    private int comboCount;

    private int feverCount;

    private int totalHiScore;

    private int totalEasyHighScore;

    private int totalBasicHighScore;

    private int totalAdvancedHighScore;

    private int totalExpertHighScore;

    private int totalMasterHighScore;

    private int totalReMasterHighScore;

    private int totalHighSync;

    private int totalEasySync;

    private int totalBasicSync;

    private int totalAdvancedSync;

    private int totalExpertSync;

    private int totalMasterSync;

    private int totalReMasterSync;

    private int playerRating;

    private int highestRating;

    private int rankAuthTailId;

    private String eventWatchedDate;

    private String webLimitDate;

    private int challengeTrackPhase;

    private int firstPlayBits;

    private String lastPlayDate;

    private int lastPlaceId;

    private String lastPlaceName;

    private int lastRegionId;

    private String lastRegionName;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String lastClientId;

    private String lastCountryCode;

    private int eventPoint;

    private int totalLv;

    private int lastLoginBonusDay;

    private int lastSurvivalBonusDay;

    private int loginBonusLv;

}
