package icu.samnyan.aqua.sega.chusan.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

class GameCharge {
    var orderId = 0
    var chargeId = 0
    var price = 0
    var startDate: LocalDateTime? = LocalDateTime.of(2019, 1, 1, 0, 0, 0)
    var endDate: LocalDateTime? = LocalDateTime.of(2099, 1, 1, 0, 0, 0)
    var salePrice = 0
    var saleStartDate: LocalDateTime? = LocalDateTime.of(2019, 1, 1, 0, 0, 0)
    var saleEndDate: LocalDateTime? = LocalDateTime.of(2099, 1, 1, 0, 0, 0)
}

class GameEvent {
    var id: Long = 0
    val type = 0
    val startDate: LocalDateTime? = LocalDateTime.of(2019, 1, 1, 0, 0, 0)
    val endDate: LocalDateTime? = LocalDateTime.of(2099, 1, 1, 0, 0, 0)
}

class GameGacha {
    var gachaId = 0
    var gachaName: String? = null
    var type = 0
    var kind = 0

    @JsonProperty("isCeiling")
    var isCeiling = false
    var ceilingCnt = 0
    var changeRateCnt1 = 0
    var changeRateCnt2 = 0
    var startDate: LocalDateTime? = LocalDateTime.of(2019, 1, 1, 0, 0, 0)
    var endDate: LocalDateTime? = LocalDateTime.of(2099, 1, 1, 0, 0, 0)
    var noticeStartDate: LocalDateTime? = LocalDateTime.of(2019, 1, 1, 0, 0, 0)
    var noticeEndDate: LocalDateTime? = LocalDateTime.of(2099, 1, 1, 0, 0, 0)
}

class GameGachaCard {
    var id: Long = 0
    var gachaId = 0
    var cardId = 0
    var rarity = 0
    var weight = 0

    @JsonProperty("isPickup")
    var isPickup = false
}

class GameLoginBonus {
    var id: Long = 0
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

class GameLoginBonusPreset {
    var id: Long = 0
    var version = 0
    var presetName: String? = null
    var isEnabled = false
}

class GameLinkedVerse {
    var id: Long = 0
    var musicId = 0
    var name: String? = null
    var startDate: LocalDateTime? = LocalDateTime.of(2019, 1, 1, 0, 0, 0)
    var endDate: LocalDateTime? = LocalDateTime.of(2099, 1, 1, 0, 0, 0)
}