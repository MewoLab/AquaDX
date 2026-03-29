package icu.samnyan.aqua.sega.chusan.model.userdata

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint

@Entity(name = "ChusanUserCMissionProgress")
@Table(name = "chusan_user_cmission_progress", uniqueConstraints = [UniqueConstraint(columnNames = ["user_id", "mission_id", "order"])])
class UserCMissionProgress : Chu3UserEntity() {
    @Column(name = "mission_id")
    @JsonIgnore
    var missionId = 0
    @Column(name = "`order`")
    var order = 0
    var stage = 0
    var progress = 0
}
