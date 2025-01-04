package icu.samnyan.aqua.sega.chusan.model.userdata

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint

@Entity(name = "ChusanUserActivity")
@Table(name = "chusan_user_activity", uniqueConstraints = [UniqueConstraint(columnNames = ["user_id", "kind", "activity_id"])])
class UserActivity : Chu3UserEntity() {
    var kind = 0
    @JsonProperty("id")
    @Column(name = "activity_id")
    var activityId = 0
    var sortNumber = 0
    var param1 = 0
    var param2 = 0
    var param3 = 0
    var param4 = 0
}
