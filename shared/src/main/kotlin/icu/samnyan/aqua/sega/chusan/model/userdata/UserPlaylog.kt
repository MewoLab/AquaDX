package icu.samnyan.aqua.sega.chusan.model.userdata

import com.fasterxml.jackson.annotation.JsonProperty
import icu.samnyan.aqua.net.games.IGenericGamePlaylog
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity(name = "ChusanUserPlaylog")
@Table(name = "chusan_user_playlog")
class UserPlaylog : Chu3UserEntity(), IGenericGamePlaylog {
    var romVersion: String? = null
    var orderId = 0
    var sortNumber = 0
    var placeId = 0
    var playDate: LocalDateTime? = null
    override var userPlayDate: LocalDateTime = LocalDateTime.now()
    override var musicId: Int = 0
    override var level: Int = 0
    var customId = 0
    var playedUserId1: Long = 0
    var playedUserId2: Long = 0
    var playedUserId3: Long = 0
    var playedUserName1: String? = null
    var playedUserName2: String? = null
    var playedUserName3: String? = null
    var playedMusicLevel1 = 0
    var playedMusicLevel2 = 0
    var playedMusicLevel3 = 0
    var playedCustom1 = 0
    var playedCustom2 = 0
    var playedCustom3 = 0
    var track = 0
    var score = 0
    @Column(name = "\"rank\"")
    var rank = 0
    override var maxCombo: Int = 0
    var maxChain = 0
    var rateTap = 0
    var rateHold = 0
    var rateSlide = 0
    var rateAir = 0
    var rateFlick = 0
    var judgeGuilty = 0
    var judgeAttack = 0
    var judgeJustice = 0
    var judgeCritical = 0
    var judgeHeaven = 0
    var eventId = 0
    var playerRating = 0
    @JsonProperty("isNewRecord")
    var isNewRecord = false
    @JsonProperty("isFullCombo")
    override var isFullCombo: Boolean = false
    var fullChainKind = 0
    @JsonProperty("isAllJustice")
    var isAllJustice = false
    @JsonProperty("isContinue")
    var isContinue = false
    @JsonProperty("isFreeToPlay")
    var isFreeToPlay = false
    var characterId = 0
    var charaIllustId = 0
    var skillId = 0
    var playKind = 0
    @JsonProperty("isClear")
    var isClear = false
    var skillLevel = 0
    var skillEffect = 0
    var placeName: String? = null
    var commonId = 0
    var regionId = 0
    var machineType = 0
    var ticketId = 0
    
    override val achievement: Int get() = score
    override val afterRating: Int get() = playerRating
    override val beforeRating: Int get() = playerRating // TODO: Implement this
    override val isAllPerfect: Boolean get() = isAllJustice
}
