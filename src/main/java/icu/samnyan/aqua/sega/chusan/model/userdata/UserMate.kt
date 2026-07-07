package icu.samnyan.aqua.sega.chusan.model.userdata

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.Entity
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint

@Entity(name = "ChusanUserMate")
@Table(name = "chusan_user_mate", uniqueConstraints = [UniqueConstraint(columnNames = ["user_id", "mate_id"])])
class UserMate : Chu3UserEntity() {
    var mateId = 0

    var playCount = 0
    var enterGardenCount = 0
    var friendshipLevel = 0
    var totalFriendshipExp = 0
    var totalUsePoint = 0

    @JsonProperty("isValid")
    var isValid = true
}