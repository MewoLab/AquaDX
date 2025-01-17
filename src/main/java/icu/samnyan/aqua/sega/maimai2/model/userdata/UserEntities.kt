@file:Suppress("unused")

package icu.samnyan.aqua.sega.maimai2.model.userdata

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import icu.samnyan.aqua.net.games.BaseEntity
import icu.samnyan.aqua.net.games.IGenericGamePlaylog
import icu.samnyan.aqua.net.games.IGenericUserMusic
import icu.samnyan.aqua.net.games.IUserEntity
import icu.samnyan.aqua.sega.general.IntegerListConverter
import jakarta.persistence.*
import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor
import java.time.LocalDateTime

@MappedSuperclass
open class Mai2UserEntity : BaseEntity(), IUserEntity<Mai2UserDetail> {
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    override var user: Mai2UserDetail = Mai2UserDetail()
}


@Table(name = "maimai2_user_npc_encount")
@Data @Entity
class Mai2MapEncountNpc : Mai2UserEntity() {

    var npcId = 0
    var musicId = 0

    @JsonIgnore
    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "extend_id")
    var userExtend: Mai2UserExtend? = null
}

@Table(name = "maimai2_user_activity")
@Data @Entity
@JsonPropertyOrder("kind", "id", "sortNumber", "param1", "param2", "param3", "param4")
class Mai2UserAct : Mai2UserEntity() {
    var kind = 0

    @JsonProperty("id")
    var activityId = 0

    var sortNumber: Long = 0
    var param1 = 0
    var param2 = 0
    var param3 = 0
    var param4 = 0
}

@Table(name = "maimai2_user_card")
@Data @Entity
class Mai2UserCard : Mai2UserEntity() {
    var cardId: Int = 0
    var cardTypeId: Int = 0
    var charaId: Int = 0
    var mapId: Int = 0
    var startDate: String = ""
    var endDate: String = ""
}

@Table(name = "maimai2_user_character")
@Data @Entity
class Mai2UserCharacter : Mai2UserEntity() {
    var characterId = 0

    @JsonInclude
    @Transient
    var point = 0

    @JsonInclude
    @Transient
    var count = 0
    var level = 0

    @JsonInclude
    @Transient
    var nextAwake = 0

    @JsonInclude
    @Transient
    var nextAwakePercent = 0

    @JsonInclude
    @Transient
    var favorite = false
    var awakening = 0
    var useCount = 0
}

@Table(name = "maimai2_user_charge", uniqueConstraints = [UniqueConstraint(columnNames = ["user_id", "charge_id"])])
@Data @Entity
@JsonPropertyOrder("chargeId", "stock", "purchaseDate", "validDate")
class Mai2UserCharge : Mai2UserEntity() {
    @Column(name = "charge_id")
    var chargeId = 0
    var stock = 0
    var purchaseDate: String = ""
    var validDate: String = ""
}

@Table(name = "maimai2_user_course")
@Data @Entity
class Mai2UserCourse : Mai2UserEntity() {
    var courseId = 0

    @JsonProperty("isLastClear")
    var isLastClear = false
    var totalRestlife = 0
    var totalAchievement = 0
    var totalDeluxscore = 0
    var playCount = 0
    var clearDate: String = ""
    var lastPlayDate: String = ""
    var bestAchievement = 0
    var bestAchievementDate: String = ""
    var bestDeluxscore = 0
    var bestDeluxscoreDate: String = ""
}

@Table(name = "maimai2_user_extend")
@Data @Entity
@JsonPropertyOrder(
    "selectMusicId",
    "selectDifficultyId",
    "categoryIndex",
    "musicIndex",
    "extraFlag",
    "selectScoreType",
    "extendContentBit",
    "isPhotoAgree",
    "isGotoCodeRead",
    "selectResultDetails",
    "sortCategorySetting",
    "sortMusicSetting",
    "playStatusSetting",
    "selectedCardList",
    "encountMapNpcList"
)
class Mai2UserExtend : Mai2UserEntity() {
    var selectMusicId = 0
    var selectDifficultyId = 0
    var categoryIndex = 0
    var musicIndex = 0
    var extraFlag = 0
    var selectScoreType = 0
    var extendContentBit: Long = 0

    @JsonProperty("isPhotoAgree")
    var isPhotoAgree = false

    @JsonProperty("isGotoCodeRead")
    var isGotoCodeRead = false
    var selectResultDetails = false
    var sortCategorySetting = 0 //enum SortTabID
    var sortMusicSetting = 0 //enum SortMusicID
    var playStatusSetting = 0 //enum PlaystatusTabID
    var selectResultScoreViewType = 0

    @Convert(converter = IntegerListConverter::class)
    var selectedCardList: List<Int> = ArrayList()

    @OneToMany(mappedBy = "userExtend", targetEntity = Mai2MapEncountNpc::class)
    var encountMapNpcList: List<Mai2MapEncountNpc> = ArrayList()
}

@Table(name = "maimai2_user_favorite")
@Data @Entity
class Mai2UserFavorite : Mai2UserEntity() {
    @JsonProperty("userId")
    var favUserId: Long = 0
    var itemKind = 0

    @Convert(converter = IntegerListConverter::class)
    var itemIdList: List<Int> = ArrayList()
}

@Table(name = "maimai2_user_friend_season_ranking")
@Data @Entity
class Mai2UserFriendSeasonRanking : Mai2UserEntity() {
    var seasonId = 0
    var point = 0

    @Column(name = "\"rank\"")
    var rank = 0
    var rewardGet = false
    var userName: String = ""
    var recordDate: String = ""
}

/**
 * This is for storing some data only use in aqua
 * @author samnyan (privateamusement@protonmail.com)
 */
@Table(name = "maimai2_user_general_data")
@Data @Entity
class Mai2UserGeneralData : Mai2UserEntity() {
    var propertyKey = ""

    @Column(columnDefinition = "TEXT")
    var propertyValue = ""
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class Mai2UserGhost {
    var name: String = ""
    var iconId = 0
    var plateId = 0
    var titleId = 0
    var rate = 0
    var udemaeRate = 0
    var courseRank = 0
    var classRank = 0
    var classValue = 0
    var playDatetime: String = ""
    var shopId = 0
    var regionCode = 0
    var typeId = 0
    var musicId = 0
    var difficulty = 0
    var version = 0
    var resultBitList: List<Byte>? = null
    var resultNum = 0
    var achievement = 0
}

@Table(name = "maimai2_user_item")
@Data @Entity
class Mai2UserItem : Mai2UserEntity() {
    var itemKind = 0
    var itemId = 0
    var stock = 0

    @JsonProperty("isValid")
    var isValid = false
}

@Suppress("EnumEntryName")
enum class Mai2ItemKind(val id: Int) {
    plate(1),
    title(2),
    icon(3),
    musicUnlock(5),
    musicMasterUnlock(6),
    musicRemasterUnlock(7),
    musicStrongUnlock(8),
    chara(9),
    partner(10),
    frame(11),
    ticket(12);

    companion object {
        val ALL: Map<Int, Mai2ItemKind> = Mai2ItemKind::class.java.enumConstants.associateBy { it.id }
    }
}

@Table(name = "maimai2_user_login_bonus")
@Data @Entity
@JsonPropertyOrder("bonusId", "point", "isCurrent", "isComplete")
class Mai2UserLoginBonus : Mai2UserEntity() {
    var bonusId = 0
    var point = 0

    @JsonProperty("isCurrent")
    var isCurrent = false

    @JsonProperty("isComplete")
    var isComplete = false
}

@Table(name = "maimai2_user_map")
@Data @Entity
@JsonPropertyOrder("mapId", "distance", "isLock", "isClear", "isComplete")
class Mai2UserMap : Mai2UserEntity() {
    var mapId = 0
    var distance = 0

    @JsonProperty("isLock")
    var isLock = false

    @JsonProperty("isClear")
    var isClear = false

    @JsonProperty("isComplete")
    var isComplete = false
}

@Table(name = "maimai2_user_music_detail")
@Data @Entity
class Mai2UserMusicDetail : Mai2UserEntity(), IGenericUserMusic {

    override var musicId = 0
    var level = 0
    var playCount = 0
    var achievement = 0
    var comboStatus = 0
    var syncStatus = 0
    var deluxscoreMax = 0
    var scoreRank = 0
    var extNum1 = 0
}

@Table(name = "maimai2_user_option")
@Data @Entity
class Mai2UserOption : Mai2UserEntity() {
    var optionKind = 0
    var noteSpeed = 0
    var slideSpeed = 0
    var touchSpeed = 0
    var tapDesign = 0
    var holdDesign = 0
    var slideDesign = 0
    var starType = 0
    var outlineDesign = 0
    var noteSize = 0
    var slideSize = 0
    var touchSize = 0
    var starRotate = 0
    var dispCenter = 0
    var dispChain = 0
    var dispRate = 0
    var dispBar = 0
    var touchEffect = 0
    var submonitorAnimation = 0
    var submonitorAchive = 0
    var submonitorAppeal = 0
    var matching = 0
    var trackSkip = 0
    var brightness = 0
    var mirrorMode = 0
    var dispJudge = 0
    var dispJudgePos = 0
    var dispJudgeTouchPos = 0
    var adjustTiming = 0
    var judgeTiming = 0
    var ansVolume = 0
    var tapHoldVolume = 0
    var criticalSe = 0
    var tapSe = 0
    var breakSe = 0
    var breakVolume = 0
    var exSe = 0
    var exVolume = 0
    var slideSe = 0
    var slideVolume = 0
    var touchHoldVolume = 0
    var damageSeVolume = 0
    var headPhoneVolume = 0
    var sortTab = 0
    var sortMusic = 0
    var outFrameType = 0
    var breakSlideVolume = 0
    var touchVolume = 0
}

@Table(name = "maimai2_user_playlog")
@Data @Entity
class Mai2UserPlaylog : Mai2UserEntity(), IGenericGamePlaylog {
    var orderId = 0
    var playlogId: Long = 0
    var version = 0
    var placeId = 0
    var placeName: String = ""
    var loginDate: Long = 0
    var playDate: String = ""
    override var userPlayDate: String = ""
    var type = 0
    override var musicId: Int = 0
    override var level: Int = 0
    var trackNo = 0
    var vsMode = 0
    var vsUserName: String = ""
    var vsStatus = 0
    var vsUserRating = 0
    var vsUserAchievement = 0
    var vsUserGradeRank = 0
    var vsRank = 0
    var playerNum = 0
    var playedUserId1: Long = 0
    var playedUserName1: String = ""
    var playedMusicLevel1 = 0
    var playedUserId2: Long = 0
    var playedUserName2: String = ""
    var playedMusicLevel2 = 0
    var playedUserId3: Long = 0
    var playedUserName3: String = ""
    var playedMusicLevel3 = 0
    var characterId1 = 0
    var characterLevel1 = 0
    var characterAwakening1 = 0
    var characterId2 = 0
    var characterLevel2 = 0
    var characterAwakening2 = 0
    var characterId3 = 0
    var characterLevel3 = 0
    var characterAwakening3 = 0
    var characterId4 = 0
    var characterLevel4 = 0
    var characterAwakening4 = 0
    var characterId5 = 0
    var characterLevel5 = 0
    var characterAwakening5 = 0
    override var achievement: Int = 0
    var deluxscore = 0
    var scoreRank = 0

    // Maximum continuous combo that the player achieved in this play.
    override var maxCombo: Int = 0

    // Maximum achievable combo in the song.
    var totalCombo = 0
    var maxSync = 0
    var totalSync = 0
    var tapCriticalPerfect = 0
    var tapPerfect = 0
    var tapGreat = 0
    var tapGood = 0
    var tapMiss = 0
    var holdCriticalPerfect = 0
    var holdPerfect = 0
    var holdGreat = 0
    var holdGood = 0
    var holdMiss = 0
    var slideCriticalPerfect = 0
    var slidePerfect = 0
    var slideGreat = 0
    var slideGood = 0
    var slideMiss = 0
    var touchCriticalPerfect = 0
    var touchPerfect = 0
    var touchGreat = 0
    var touchGood = 0
    var touchMiss = 0
    var breakCriticalPerfect = 0
    var breakPerfect = 0
    var breakGreat = 0
    var breakGood = 0
    var breakMiss = 0

    @JsonProperty("isTap")
    var isTap = false

    @JsonProperty("isHold")
    var isHold = false

    @JsonProperty("isSlide")
    var isSlide = false

    @JsonProperty("isTouch")
    var isTouch = false

    @JsonProperty("isBreak")
    var isBreak = false

    @JsonProperty("isCriticalDisp")
    var isCriticalDisp = false

    @JsonProperty("isFastLateDisp")
    var isFastLateDisp = false
    var fastCount = 0
    var lateCount = 0

    @JsonProperty("isAchieveNewRecord")
    var isAchieveNewRecord = false

    @JsonProperty("isDeluxscoreNewRecord")
    var isDeluxscoreNewRecord = false
    var comboStatus = 0
    var syncStatus = 0

    @JsonProperty("isClear")
    var isClear = false
    override var beforeRating: Int = 0
    override var afterRating: Int = 0
    var beforeGrade = 0
    var afterGrade = 0
    var afterGradeRank = 0
    var beforeDeluxRating = 0
    var afterDeluxRating = 0

    @JsonProperty("isPlayTutorial")
    var isPlayTutorial = false

    @JsonProperty("isEventMode")
    var isEventMode = false

    @JsonProperty("isFreedomMode")
    var isFreedomMode = false
    var playMode = 0

    @JsonProperty("isNewFree")
    var isNewFree = false

    var trialPlayAchievement = 0
    var extNum1 = 0
    var extNum2 = 0
    var extNum4 = 0

    @JsonProperty("extBool1")
    var extBool1 = false
    @JsonProperty("extBool2")
    var extBool2 = false

    override val isFullCombo: Boolean
        get() = maxCombo == totalCombo

    override val isAllPerfect: Boolean
        get() = tapMiss + tapGood + tapGreat == 0 && 
            holdMiss + holdGood + holdGreat == 0 && 
            slideMiss + slideGood + slideGreat == 0 && 
            touchMiss + touchGood + touchGreat == 0 &&
            breakMiss + breakGood + breakGreat == 0
}

@Table(name = "maimai2_user_print_detail")
@Data @Entity
class Mai2UserPrintDetail : Mai2UserEntity() {
    var orderId: Long = 0
    var printNumber = 0
    var printDate: String = ""
    var serialId: String = ""
    var placeId = 0
    var clientId: String = ""
    var printerSerialId: String = ""

    @ManyToOne
    @JoinColumn(name = "user_card_id")
    var userCard: Mai2UserCard? = null
    var cardRomVersion = 0
    var isHolograph = false
    var printOption1 = false
    var printOption2 = false
    var printOption3 = false
    var printOption4 = false
    var printOption5 = false
    var printOption6 = false
    var printOption7 = false
    var printOption8 = false
    var printOption9 = false
    var printOption10 = false
    var created: String = ""
}

data class Mai2UserRate(
    var musicId: Int = 0,
    var level: Int = 0,
    var romVersion: Int = 0,
    var achievement: Int = 0,
)

@Table(name = "maimai2_user_udemae")
@Data @Entity
class Mai2UserUdemae : Mai2UserEntity() {
    var rate = 0
    var maxRate = 0
    var classValue = 0
    var maxClassValue = 0
    var totalWinNum = 0
    var totalLoseNum = 0
    var maxWinNum = 0
    var maxLoseNum = 0
    var winNum = 0
    var loseNum = 0
    var npcTotalWinNum = 0
    var npcTotalLoseNum = 0
    var npcMaxWinNum = 0
    var npcMaxLoseNum = 0
    var npcWinNum = 0
    var npcLoseNum = 0
}

@Table(name = "maimai2_user_kaleidx")
@Data @Entity
class Mai2UserKaleidx : Mai2UserEntity() {
    var gateId = 0
    var isGateFound = false
    var isKeyFound = false
    var isClear = false
    var totalRestLife = 0
    var totalAchievement = 0
    var totalDeluxscore = 0
    var bestAchievement = 0
    var bestDeluxscore = 0
    var bestAchievementDate: LocalDateTime = LocalDateTime.MIN
    var bestDeluxscoreDate: LocalDateTime = LocalDateTime.MIN
    var playCount = 0
    var clearDate: LocalDateTime = LocalDateTime.MIN
    var lastPlayDate: LocalDateTime = LocalDateTime.MIN
    var isInfoWatched = false
}