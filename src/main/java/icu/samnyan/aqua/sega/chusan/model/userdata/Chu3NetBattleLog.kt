package icu.samnyan.aqua.sega.chusan.model.userdata

import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity(name = "ChusanNetBattleLog")
@Table(name = "chusan_net_battle_log")
class Chu3NetBattleLog(
    var roomId: Long = 0,
    var track: Long = 0,
    var selectUserId: Long = 0,
    var selectUserName: String = "",
    var opponentUserId1: Long = 0,
    var opponentUserId2: Long = 0,
    var opponentUserId3: Long = 0,
    var opponentUserName1: String = "",
    var opponentUserName2: String = "",
    var opponentUserName3: String = "",
    var opponentRegionId1: Int = 0,
    var opponentRegionId2: Int = 0,
    var opponentRegionId3: Int = 0,
    var opponentRating1: Int = 0,
    var opponentRating2: Int = 0,
    var opponentRating3: Int = 0,
    var opponentBattleRankId1: Int = 0,
    var opponentBattleRankId2: Int = 0,
    var opponentBattleRankId3: Int = 0,
    var opponentClassEmblemMedal1: Int = 0,
    var opponentClassEmblemMedal2: Int = 0,
    var opponentClassEmblemMedal3: Int = 0,
    var opponentClassEmblemBase1: Int = 0,
    var opponentClassEmblemBase2: Int = 0,
    var opponentClassEmblemBase3: Int = 0,
    var opponentScore1: Int = 0,
    var opponentScore2: Int = 0,
    var opponentScore3: Int = 0,
    var opponentCharaIllustId1: Int = 0,
    var opponentCharaIllustId2: Int = 0,
    var opponentCharaIllustId3: Int = 0,
    var opponentCharaLv1: Int = 0,
    var opponentCharaLv2: Int = 0,
    var opponentCharaLv3: Int = 0,
    var opponentRatingEffectColorId1: Int = 0,
    var opponentRatingEffectColorId2: Int = 0,
    var opponentRatingEffectColorId3: Int = 0,
    var battleRuleId: Int = 0,
    var monthPoLong: Int = 0,
    var eventPoLong: Int = 0
) : Chu3UserEntity()
