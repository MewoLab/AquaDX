package icu.samnyan.aqua.sega.chusan.model.userdata

import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity(name = "ChusanUserGacha")
@Table(name = "chusan_user_gacha")
class UserGacha : Chu3UserEntity() {
    var gachaId = 0
    var totalGachaCnt = 0
    var ceilingGachaCnt = 0
    var dailyGachaCnt = 0
    var fiveGachaCnt = 0
    var elevenGachaCnt = 0
    var dailyGachaDate: LocalDateTime? = null
}
