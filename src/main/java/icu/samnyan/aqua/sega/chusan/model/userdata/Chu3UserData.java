package icu.samnyan.aqua.sega.chusan.model.userdata;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import icu.samnyan.aqua.net.games.IUserData;
import icu.samnyan.aqua.sega.chusan.model.response.data.UserEmoney;
import icu.samnyan.aqua.sega.general.model.Card;
import icu.samnyan.aqua.sega.util.jackson.AccessCodeSerializer;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Entity(name = "ChusanUserData")
@Table(name = "chusan_user_data")
@Getter
@Setter
@NoArgsConstructor
@JsonPropertyOrder({
        "accessCode",
        "userName",
        "level",
        "reincarnationNum",
        "exp",
        "point",
        "totalPoint",
        "playCount",
        "multiPlayCount",
        "playerRating",
        "highestRating",
        "nameplateId",
        "frameId",
        "characterId",
        "trophyId",
        "playedTutorialBit",
        "firstTutorialCancelNum",
        "masterTutorialCancelNum",
        "totalMapNum",
        "totalHiScore",
        "totalBasicHighScore",
        "totalAdvancedHighScore",
        "totalExpertHighScore",
        "totalMasterHighScore",
        "totalUltimaHighScore",
        "eventWatchedDate",
        "friendCount",
        "firstGameId",
        "firstRomVersion",
        "firstDataVersion",
        "firstPlayDate",
        "lastGameId",
        "lastRomVersion",
        "lastDataVersion",
        "lastPlayDate",
        "lastPlaceId",
        "lastPlaceName",
        "lastRegionId",
        "lastRegionName",
        "lastAllNetId",
        "lastClientId",
        "lastCountryCode",
        "userNameEx",
        "compatibleCmVersion",
        "medal",
        "mapIconId",
        "voiceId",
        "avatarWear",
        "avatarHead",
        "avatarFace",
        "avatarSkin",
        "avatarItem",
        "avatarFront",
        "avatarBack",
        "classEmblemBase",
        "classEmblemMedal",
        "stockedGridCount",
        "exMapLoopCount",
        "netBattlePlayCount",
        "netBattleWinCount",
        "netBattleLoseCount",
        "netBattleConsecutiveWinCount",
        "charaIllustId",
        "skillId",
        "overPowerPoint",
        "overPowerRate",
        "overPowerLowerRank",
        "avatarPoint",
        "battleRankId",
        "battleRankPoint",
        "eliteRankPoint",
        "netBattle1stCount",
        "netBattle2ndCount",
        "netBattle3rdCount",
        "netBattle4thCount",
        "netBattleCorrection",
        "netBattleErrCnt",
        "netBattleHostErrCnt",
        "battleRewardStatus",
        "battleRewardIndex",
        "battleRewardCount",
        "ext1",
        "ext2",
        "ext3",
        "ext4",
        "ext5",
        "ext6",
        "ext7",
        "ext8",
        "ext9",
        "ext10",
        "extStr1",
        "extStr2",
        "extLong1",
        "extLong2",
        "rankUpChallengeResults",
        "isNetBattleHost",
        "netBattleEndState" })
public class Chu3UserData implements Serializable, IUserData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private long id;

    @JsonSerialize(using = AccessCodeSerializer.class)
    @JsonProperty(value = "accessCode", access = JsonProperty.Access.READ_ONLY)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", unique = true)
    private Card card;

    private String userName;

    private int level;

    private int reincarnationNum;

    private String exp;

    private long point;

    private long totalPoint;

    private int playCount;

    private int multiPlayCount;

    private int playerRating;

    private int highestRating;

    private int nameplateId;

    private int frameId;

    private int characterId;

    private int trophyId;

    private int playedTutorialBit;

    private int firstTutorialCancelNum;

    private int masterTutorialCancelNum;

    private int totalMapNum;

    private long totalHiScore;

    private long totalBasicHighScore;

    private long totalAdvancedHighScore;

    private long totalExpertHighScore;

    private long totalMasterHighScore;

    private long totalUltimaHighScore;

    private LocalDateTime eventWatchedDate;

    private int friendCount;

    private String firstGameId;

    private String firstRomVersion;

    private String firstDataVersion;

    private LocalDateTime firstPlayDate;

    private String lastGameId;

    private String lastRomVersion;

    private String lastDataVersion;

    @JsonIgnore
    private LocalDateTime lastLoginDate;

    private LocalDateTime lastPlayDate;

    private int lastPlaceId;

    private String lastPlaceName;

    private String lastRegionId;

    private String lastRegionName;

    private String lastAllNetId;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String lastClientId;

    private String lastCountryCode;

    private String userNameEx;

    private String compatibleCmVersion;

    private int medal;

    private int mapIconId;

    private int voiceId;

    private int avatarWear;

    private int avatarHead;

    private int avatarFace;

    private int avatarSkin;

    private int avatarItem;

    private int avatarFront;

    private int avatarBack;

    private int classEmblemBase;

    private int classEmblemMedal;

    private int stockedGridCount;

    private int exMapLoopCount;

    private int netBattlePlayCount;

    private int netBattleWinCount;

    private int netBattleLoseCount;

    private int netBattleConsecutiveWinCount;

    private int charaIllustId;

    private int skillId;

    private int overPowerPoint;

    private int overPowerRate;

    private int overPowerLowerRank;

    private int avatarPoint;

    private int battleRankId;

    private int battleRankPoint;

    private int eliteRankPoint;

    private int netBattle1stCount;

    private int netBattle2ndCount;

    private int netBattle3rdCount;

    private int netBattle4thCount;

    private int netBattleCorrection;

    private int netBattleErrCnt;

    private int netBattleHostErrCnt;

    private int battleRewardStatus;

    private int battleRewardIndex;

    private int battleRewardCount;

    private int ext1;

    private int ext2;

    private int ext3;

    private int ext4;

    private int ext5;

    private int ext6;

    private int ext7;

    private int ext8;

    private int ext9;

    private int ext10;

    private String extStr1;

    private String extStr2;

    private long extLong1;

    private long extLong2;

    @JsonInclude
    @Transient
    private List<Object> rankUpChallengeResults;

    @JsonProperty("isNetBattleHost")
    private boolean isNetBattleHost;

    private int netBattleEndState;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Transient
    private UserEmoney userEmoney;

    @Override
    public long getTotalScore() {
        return totalHiScore;
    }
}
