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
    val playCount = 0
    val level = 1
    val friendshipExp = 0
    @JsonProperty("isValid")
    val isValid = true
    @JsonProperty("isNewMark")
    val isNewMark = true
    val exMaxLv = 0
    val assignIllust = 0
    val param1 = 0
    val param2 = 0
}
