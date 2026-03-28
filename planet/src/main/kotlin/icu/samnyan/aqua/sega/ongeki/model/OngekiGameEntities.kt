package icu.samnyan.aqua.sega.ongeki.model

import com.fasterxml.jackson.annotation.JsonProperty

class GameCard {
    var cardId: Long = 0
    var name: String = ""
    var nickName: String = ""
    var attribute: String = ""
    var charaId = 0
    var school: String = ""
    var gakunen: String = ""
    var rarity: String = ""
    // csv
    var levelParam: String = ""
    var skillId = 0
    var choKaikaSkillId = 0
    var cardNumber: String = ""
    var version: String = ""
}

class GameChara {
    var name: String = ""
    var cv: String = ""
    var modelId = 0
}

class GameEvent {
    var id: Long = 0
}

class GameMusic {
    var id: Long = 0
    var name: String = ""
    var sortName: String = ""
    var artistName: String = ""
    var genre: String = ""
    var bossCardId = 0
    var bossLevel = 0
    var level0: String = ""
    var level1: String = ""
    var level2: String = ""
    var level3: String = ""
    var level4: String = ""
}

class GamePoint {
    var id: Long = 0
    var type: OgkGpProductID = OgkGpProductID.A_Credit1
    var cost: Int = 0
    val startDate = "2000-01-01 05:00:00.0"
    val endDate = "2099-01-01 05:00:00.0"
}

class GamePresent {
    var id: Long = 0
    var presentName: String = ""
    var rewardId: Int = 0 // count
    var stock: Int = 0 // acquisitionCondition
    var message: String = ""
    val startDate = "2000-01-01 05:00:00.0"
    val endDate = "2099-01-01 05:00:00.0"
}

class GameReward {
    var id: Long = 0
    var itemKind: OgkItemType = OgkItemType.None
    var itemId = 0
}

class GameSkill {
    var id: Long = 0
    var name: String = ""
    var category: String = ""
    var info: String = ""
}

class GameGachaCard {
    var cardId: Long = 0
    var gachaId: Long = 0
    var rarity: Int = 0
    var weight: Int = 0

    @JsonProperty("isPickup")
    var isPickup: Boolean = false

    @JsonProperty("isSelect")
    var isSelect: Boolean = false
}

class GameGacha {
    var gachaId: Long = 0
    var gachaName: String = ""
    var type: Int = 0
    var kind: Int = 0
    var maxSelectPoint: Int = 0
    var ceilingCnt: Int = 0
    var changeRateCnt1: Int = 0
    var changeRateCnt2: Int = 0
    var startDate: String = "2020-01-01 00:00:00"
    var endDate: String = "2099-01-01 00:00:00"
    var noticeStartDate: String = "2020-01-01 00:00:00"
    var noticeEndDate: String = "2099-01-01 00:00:00"
    var convertEndDate: String = "2099-01-01 00:00:00"

    @JsonProperty("isCeiling")
    var isCeiling: Boolean = false
}