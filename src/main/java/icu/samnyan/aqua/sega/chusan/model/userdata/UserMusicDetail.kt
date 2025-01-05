package icu.samnyan.aqua.sega.chusan.model.userdata

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import icu.samnyan.aqua.net.games.IGenericUserMusic
import icu.samnyan.aqua.sega.util.jackson.BooleanToIntegerDeserializer
import jakarta.persistence.Entity
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Entity(name = "ChusanUserMusicDetail")
@Table(
    name = "chusan_user_music_detail",
    uniqueConstraints = [UniqueConstraint(columnNames = ["user_id", "music_id", "level"])]
)
class UserMusicDetail : Chu3UserEntity(), IGenericUserMusic {
    override var musicId = 0
    var level = 0
    var playCount = 0
    var scoreMax = 0
    var missCount = 0
    var maxComboCount = 0

    @JsonProperty("isFullCombo")
    var isFullCombo = false

    @JsonProperty("isAllJustice")
    var isAllJustice = false

    @JsonDeserialize(using = BooleanToIntegerDeserializer::class)
    @JsonProperty("isSuccess")
    var isSuccess = 0

    var fullChain = 0
    var maxChain = 0
    var scoreRank = 0

    @JsonProperty("isLock")
    var isLock = false

    var theoryCount = 0
    var ext1 = 0
}
