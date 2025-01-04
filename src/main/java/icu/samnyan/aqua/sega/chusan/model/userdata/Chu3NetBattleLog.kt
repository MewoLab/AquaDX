package icu.samnyan.aqua.sega.chusan.model.userdata

import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity(name = "ChusanNetBattleLog")
@Table(name = "chusan_net_battle_log")
class Chu3NetBattleLog(
    val roomId: Long = 0,
    val track: Long = 0,
    val selectUserId: Long = 0,
    val selectUserName: String = "",
    val opponentUserId1: Long = 0,
    val opponentUserId2: Long = 0,
    val opponentUserId3: Long = 0,
    val opponentUserName1: String = "",
    val opponentUserName2: String = "",
    val opponentUserName3: String = "",
    val opponentRegionId1: Int = 0,
    val opponentRegionId2: Int = 0,
    val opponentRegionId3: Int = 0,
    val opponentRating1: Int = 0,
    val opponentRating2: Int = 0,
    val opponentRating3: Int = 0,
    val opponentBattleRankId1: Int = 0,
    val opponentBattleRankId2: Int = 0,
    val opponentBattleRankId3: Int = 0,
    val opponentClassEmblemMedal1: Int = 0,
    val opponentClassEmblemMedal2: Int = 0,
    val opponentClassEmblemMedal3: Int = 0,
    val opponentClassEmblemBase1: Int = 0,
    val opponentClassEmblemBase2: Int = 0,
    val opponentClassEmblemBase3: Int = 0,
    val opponentScore1: Int = 0,
    val opponentScore2: Int = 0,
    val opponentScore3: Int = 0,
    val opponentCharaIllustId1: Int = 0,
    val opponentCharaIllustId2: Int = 0,
    val opponentCharaIllustId3: Int = 0,
    val opponentCharaLv1: Int = 0,
    val opponentCharaLv2: Int = 0,
    val opponentCharaLv3: Int = 0,
    val opponentRatingEffectColorId1: Int = 0,
    val opponentRatingEffectColorId2: Int = 0,
    val opponentRatingEffectColorId3: Int = 0,
    val battleRuleId: Int = 0,
    val monthPoLong: Int = 0,
    val eventPoLong: Int = 0
) : Chu3UserEntity()
