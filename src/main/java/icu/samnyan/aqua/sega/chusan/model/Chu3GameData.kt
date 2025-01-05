package icu.samnyan.aqua.sega.chusan.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import icu.samnyan.aqua.net.games.BaseEntity
import jakarta.persistence.*
import java.time.LocalDateTime


// BaseEntity does not expose id to json, but IdExposedEntity does
@MappedSuperclass
class IdExposedEntity {
    @Id
    var id: Long = 0
}

@Entity(name = "ChusanGameCharge")
@Table(name = "chusan_game_charge")
class GameCharge: BaseEntity() {
    var orderId = 0

    @Column(unique = true)
    var chargeId = 0
    var price = 0
    var startDate: LocalDateTime? = null
    var endDate: LocalDateTime? = null
    var salePrice = 0
    var saleStartDate: LocalDateTime? = null
    var saleEndDate: LocalDateTime? = null
}

@Entity(name = "ChusanGameEvent")
@Table(name = "chusan_game_event")
class GameEvent: IdExposedEntity() {
    val type = 0
    val startDate: LocalDateTime? = null
    val endDate: LocalDateTime? = null

    @JsonIgnore
    val enable = false
}

@Entity(name = "ChusanGameGacha")
@Table(name = "chusan_game_gacha")
class GameGacha : IdExposedEntity() {
    var gachaId = 0
    var gachaName: String? = null
    var type = 0
    var kind = 0

    @JsonProperty("isCeiling")
    var isCeiling = false
    var ceilingCnt = 0
    var changeRateCnt1 = 0
    var changeRateCnt2 = 0
    var startDate: LocalDateTime? = null
    var endDate: LocalDateTime? = null
    var noticeStartDate: LocalDateTime? = null
    var noticeEndDate: LocalDateTime? = null
}

@Entity(name = "ChusanGameGachaCard")
@Table(name = "chusan_game_gacha_card")
class GameGachaCard : IdExposedEntity() {
    var gachaId = 0
    var cardId = 0
    var rarity = 0
    var weight = 0

    @JsonProperty("isPickup")
    var isPickup = false
}

@Entity(name = "ChusanGameLoginBonus")
@Table(name = "chusan_game_login_bonus")
class GameLoginBonus : IdExposedEntity() {
    var version = 0
    var presetId = 0
    var loginBonusId = 0
    var loginBonusName: String? = null
    var presentId = 0
    var presentName: String? = null
    var itemNum = 0
    var needLoginDayCount = 0
    var loginBonusCategoryType = 0
}

@Entity(name = "ChusanGameLoginBonusPreset")
@Table(name = "chusan_game_login_bonus_preset")
class GameLoginBonusPreset : IdExposedEntity() {
    var version = 0
    var presetName: String? = null
    var isEnabled = false
}