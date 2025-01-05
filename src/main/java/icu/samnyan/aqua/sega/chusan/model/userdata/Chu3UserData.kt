package icu.samnyan.aqua.sega.chusan.model.userdata

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import icu.samnyan.aqua.net.games.BaseEntity
import icu.samnyan.aqua.net.games.IUserData
import icu.samnyan.aqua.sega.chusan.model.request.UserEmoney
import icu.samnyan.aqua.sega.general.model.Card
import icu.samnyan.aqua.sega.util.jackson.AccessCodeSerializer
import jakarta.persistence.*
import lombok.NoArgsConstructor
import java.time.LocalDateTime

@Entity(name = "ChusanUserData")
@Table(name = "chusan_user_data")
@NoArgsConstructor
class Chu3UserData : BaseEntity(), IUserData {
    @JsonSerialize(using = AccessCodeSerializer::class)
    @JsonProperty(value = "accessCode", access = JsonProperty.Access.READ_ONLY)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", unique = true)
    override var card: Card? = null

    override var userName: String = ""
    var level = 0
    var reincarnationNum = 0
    var exp: String = ""
    var point: Long = 0
    var totalPoint: Long = 0
    var playCount = 0
    var multiPlayCount = 0

    override var playerRating: Int = 0

    override var highestRating: Int = 0
    var nameplateId = 0
    var frameId = 0
    var characterId = 0
    var trophyId = 0
    var playedTutorialBit = 0
    var firstTutorialCancelNum = 0
    var masterTutorialCancelNum = 0
    var totalMapNum = 0

    var totalHiScore: Long = 0
    var totalBasicHighScore: Long = 0
    var totalAdvancedHighScore: Long = 0
    var totalExpertHighScore: Long = 0
    var totalMasterHighScore: Long = 0
    var totalUltimaHighScore: Long = 0
    var eventWatchedDate: LocalDateTime = LocalDateTime.now()
    var friendCount = 0
    var firstGameId: String = ""
    var firstRomVersion: String = ""
    var firstDataVersion: String = ""

    override var firstPlayDate: LocalDateTime = LocalDateTime.now()
    var lastGameId: String = ""

    override var lastRomVersion: String = ""
    var lastDataVersion: String = ""

    @JsonIgnore
    var lastLoginDate: LocalDateTime = LocalDateTime.now()

    override var lastPlayDate: LocalDateTime = LocalDateTime.now()
    var lastPlaceId = 0
    var lastPlaceName: String = ""
    var lastRegionId: String = ""
    var lastRegionName: String = ""
    var lastAllNetId: String = ""

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    override var lastClientId: String = ""
    var lastCountryCode: String = ""
    var userNameEx: String = ""
    var compatibleCmVersion: String = ""
    var medal = 0
    var mapIconId = 0
    var voiceId = 0
    var avatarWear = 0
    var avatarHead = 0
    var avatarFace = 0
    var avatarSkin = 0
    var avatarItem = 0
    var avatarFront = 0
    var avatarBack = 0
    var classEmblemBase = 0
    var classEmblemMedal = 0
    var stockedGridCount = 0
    var exMapLoopCount = 0
    var netBattlePlayCount = 0
    var netBattleWinCount = 0
    var netBattleLoseCount = 0
    var netBattleConsecutiveWinCount = 0
    var charaIllustId = 0
    var skillId = 0
    var overPowerPoint = 0
    var overPowerRate = 0
    var overPowerLowerRank = 0
    var avatarPoint = 0
    var battleRankId = 0
    var battleRankPoint = 0
    var eliteRankPoint = 0
    var netBattle1stCount = 0
    var netBattle2ndCount = 0
    var netBattle3rdCount = 0
    var netBattle4thCount = 0
    var netBattleCorrection = 0
    var netBattleErrCnt = 0
    var netBattleHostErrCnt = 0
    var battleRewardStatus = 0
    var battleRewardIndex = 0
    var battleRewardCount = 0
    var ext1 = 0
    var ext2 = 0
    var ext3 = 0
    var ext4 = 0
    var ext5 = 0
    var ext6 = 0
    var ext7 = 0
    var ext8 = 0
    var ext9 = 0
    var ext10 = 0
    var extStr1: String = ""
    var extStr2: String = ""
    var extLong1: Long = 0
    var extLong2: Long = 0

    @JsonInclude
    @Transient
    var rankUpChallengeResults: List<Any>? = null

    // When serialized, this field should be "isNetBattleHost", not "netBattleHost"
    @JsonProperty("isNetBattleHost")
    var isNetBattleHost = false
    var netBattleEndState = 0

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Transient
    var userEmoney: UserEmoney? = null

    override val totalScore get() = totalHiScore
}
