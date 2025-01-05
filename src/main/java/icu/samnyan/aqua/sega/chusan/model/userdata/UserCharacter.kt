package icu.samnyan.aqua.sega.chusan.model.userdata

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint

@Entity(name = "ChusanUserCharacter")
@Table(name = "chusan_user_character", uniqueConstraints = [UniqueConstraint(columnNames = ["user_id", "character_id"])])
class UserCharacter : Chu3UserEntity() {
    @Column(name = "character_id")
    var characterId = 0
    var playCount = 0
    var level = 1
    var friendshipExp = 0
    @JsonProperty("isValid")
    var isValid = true
    @JsonProperty("isNewMark")
    var isNewMark = true
    var exMaxLv = 0
    var assignIllust = 0
    var param1 = 0
    var param2 = 0
}
