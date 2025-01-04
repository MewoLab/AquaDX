package icu.samnyan.aqua.sega.chusan.model.userdata

import ext.DATE_2018
import icu.samnyan.aqua.net.games.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity(name = "ChusanUserLoginBonus")
@Table(name = "chusan_user_login_bonus")
class UserLoginBonus(
    var version: Int = 0,
    var user: Int = 0,
    var presetId: Int = 0,
    var bonusCount: Int = 0,
    var lastUpdateDate: LocalDateTime = DATE_2018,
    var isWatched: Boolean = false, // TODO: Check if this should be "watched" or "isWatched" in json
    var isFinished: Boolean = false,
) : BaseEntity()
