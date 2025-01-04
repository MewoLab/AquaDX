package icu.samnyan.aqua.sega.chusan.model.userdata
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.Entity
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import java.time.LocalDateTime

@Entity(name = "ChusanUserCourse")
@Table(name = "chusan_user_course", uniqueConstraints = [UniqueConstraint(columnNames = ["user_id", "course_id"])])
class UserCourse : Chu3UserEntity() {
    var courseId = 0
    var classId = 0
    var playCount = 0
    var theoryCount = 0
    var scoreMax = 0
    @JsonProperty("isFullCombo")
    var isFullCombo = false
    @JsonProperty("isAllJustice")
    var isAllJustice = false
    @JsonProperty("isSuccess")
    var isSuccess = false
    var scoreRank = 0
    var eventId = 0
    var lastPlayDate: LocalDateTime? = null
    var param1 = 0
    var param2 = 0
    var param3 = 0
    var param4 = 0
    var orderId = 0
    var playerRating = 0
    @JsonProperty("isClear")
    var isClear = false
}
