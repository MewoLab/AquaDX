package icu.samnyan.aqua.sega.chusan.model.userdata

import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity(name = "ChusanUserCMission")
@Table(name = "chusan_user_cmission")
class UserCMission : Chu3UserEntity() {
    var missionId = 0
    var point = 0
}
