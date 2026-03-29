package icu.samnyan.aqua.sega.chusan.model.userdata

import jakarta.persistence.Entity
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint


@Entity(name = "ChusanUserChallenge")
@Table(name = "chusan_user_challenge", uniqueConstraints = [UniqueConstraint(columnNames = ["user_id", "unlock_challenge_id"])])
class Chu3UserChallenge : Chu3UserEntity() {
    var unlockChallengeId = 0
    var status = 0
    var clearCourseId = 0
    var conditionType = 0
    var score = 0
    var life = 0
    var clearDate = "" // len 20 YYYY-MM-DD HH:MM:SS
}
