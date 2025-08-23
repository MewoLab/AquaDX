package icu.samnyan.aqua.sega.ongeki.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*

@Entity(name = "OngekiGameCard")
@Table(name = "ongeki_game_card")
class GameCard {
    @Id
    var id: Long = 0
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


@Entity(name = "OngekiGameChara")
@Table(name = "ongeki_game_chara")
class GameChara {
    @Id
    var id: Long = 0
    var name: String = ""
    var cv: String = ""
    var modelId = 0
}


@Entity(name = "OngekiGameEvent")
@Table(name = "ongeki_game_event")
class GameEvent {
    @Id
    var id: Long = 0
}


@Entity(name = "OngekiGameMusic")
@Table(name = "ongeki_game_music")
class GameMusic {
    @Id
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


@Entity(name = "OngekiGamePoint")
@Table(name = "ongeki_game_point", uniqueConstraints = [UniqueConstraint(columnNames = ["type"])])
class GamePoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    var id: Long = 0
    @Enumerated(EnumType.ORDINAL)
    var type: OgkGpProductID = OgkGpProductID.A_Credit1
    var cost: Int = 0
    val startDate = "2000-01-01 05:00:00.0"
    val endDate = "2099-01-01 05:00:00.0"
}


@Entity(name = "OngekiGamePresent")
@Table(name = "ongeki_game_present")
class GamePresent {
    @Id
    var id: Long = 0
    var presentName: String = ""
    var rewardId: Int = 0 // count
    var stock: Int = 0 // acquisitionCondition
    var message: String = ""
    val startDate = "2000-01-01 05:00:00.0"
    val endDate = "2099-01-01 05:00:00.0"
}


@Entity(name = "OngekiGameReward")
@Table(name = "ongeki_game_reward")
class GameReward {
    @Id
    var id: Long = 0

    @Enumerated(EnumType.ORDINAL)
    var itemKind: OgkItemType = OgkItemType.None
    var itemId = 0
}


@Entity(name = "OngekiGameSkill")
@Table(name = "ongeki_game_skill")
class GameSkill {
    @Id
    var id: Long = 0
    var name: String = ""
    var category: String = ""
    var info: String = ""
}

@Entity(name = "OngekiGameGachaCard")
@Table(name = "ongeki_game_gacha_card")
class GameGachaCard {
    @Id
    var cardId: Long = 0
    var gachaId: Long = 0
    var rarity: Int = 0
    var weight: Int = 0

    @JsonProperty("isPickup")
    var isPickup: Boolean = false

    @JsonProperty("isSelect")
    var isSelect: Boolean = false
}

@Entity(name = "OngekiGameGacha")
@Table(name = "ongeki_game_gacha")
class GameGacha {
    @Id
    var gachaId: Long = 0
    var gachaName: String = ""
    var type: Int = 0
    var kind: Int = 0
    var maxSelectPoint: Int = 0
    var ceilingCnt: Int = 0
    var changeRateCnt1: Int = 0
    var changeRateCnt2: Int = 0
    var startDate: String = ""
    var endDate: String = ""
    var noticeStartDate: String = ""
    var noticeEndDate: String = ""
    var convertEndDate: String = ""

    @JsonProperty("isCeiling")
    var isCeiling: Boolean = false
}