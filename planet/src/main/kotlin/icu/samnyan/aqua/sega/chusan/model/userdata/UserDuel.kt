package icu.samnyan.aqua.sega.chusan.model.userdata
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import java.time.LocalDateTime

@Entity(name = "ChusanUserDuel")
@Table(name = "chusan_user_duel", uniqueConstraints = [UniqueConstraint(columnNames = ["user_id", "duel_id"])])
class UserDuel : Chu3UserEntity() {
    @Column(name = "duel_id")
    var duelId = 0
    var progress = 0
    var point = 0
    @JsonProperty("isClear")
    var isClear = false
    var lastPlayDate: LocalDateTime? = null
    var param1 = 0
    var param2 = 0
    var param3 = 0
    var param4 = 0
}
