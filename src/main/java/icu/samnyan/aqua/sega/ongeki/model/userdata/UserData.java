package icu.samnyan.aqua.sega.ongeki.model.userdata;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import icu.samnyan.aqua.net.games.IUserData;
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
@Entity(name = "OngekiUserData")
@Table(name = "ongeki_user_data")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserData implements Serializable, IUserData {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private long id;

    @JsonSerialize(using = AccessCodeSerializer.class)
    @JsonProperty(value = "accessCode", access = JsonProperty.Access.READ_ONLY)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aime_card_id", unique = true)
    private Card card;
    // Access code in card

    private String userName;

    private int level;

    private int reincarnationNum;

    private long exp;

    private long point;

    private long totalPoint;

    private int playCount;

    private int jewelCount;

    private int totalJewelCount;

    private int medalCount;

    private int playerRating;

    private int highestRating;

    private int battlePoint;

    private int bestBattlePoint;

    private int overDamageBattlePoint;

    @JsonProperty("isDialogWatchedSuggestMemory")
    private boolean isDialogWatchedSuggestMemory;

    private int nameplateId;

    private int trophyId;

    private int cardId;

    private int characterId;

    private int characterVoiceNo;

    private int tabSetting;

    private int tabSortSetting;

    private int cardCategorySetting;

    private int cardSortSetting;

    private int rivalScoreCategorySetting;

    private int playedTutorialBit;

    private int firstTutorialCancelNum;

    private long sumTechHighScore;

    private long sumTechBasicHighScore;

    private long sumTechAdvancedHighScore;

    private long sumTechExpertHighScore;

    private long sumTechMasterHighScore;

    private long sumTechLunaticHighScore;

    private long sumBattleHighScore;

    private long sumBattleBasicHighScore;

    private long sumBattleAdvancedHighScore;

    private long sumBattleExpertHighScore;

    private long sumBattleMasterHighScore;

    private long sumBattleLunaticHighScore;

    private String eventWatchedDate;

    private String cmEventWatchedDate;

    private String firstGameId;

    private String firstRomVersion;

    private String firstDataVersion;

    private String firstPlayDate;

    private String lastGameId;

    private String lastRomVersion;

    private String lastDataVersion;

    private String compatibleCmVersion;

    private String lastPlayDate;

    private int lastPlaceId;

    private String lastPlaceName;

    private int lastRegionId;

    private String lastRegionName;

    private int lastAllNetId;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String lastClientId;

    private int lastUsedDeckId;

    private int lastPlayMusicLevel;

    private int lastEmoneyBrand;

    @Override
    public long getTotalScore() {
        return sumTechHighScore;
    }
}
