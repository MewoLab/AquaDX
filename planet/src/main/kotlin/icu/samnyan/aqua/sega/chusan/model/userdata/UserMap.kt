package icu.samnyan.aqua.sega.chusan.model.userdata

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.Entity
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint

@Entity(name = "ChusanUserMapArea")
@Table(name = "chusan_user_map_area", uniqueConstraints = [UniqueConstraint(columnNames = ["user_id", "map_area_id"])])
class UserMap : Chu3UserEntity() {
    var mapAreaId = 0
    var position = 0

    @JsonProperty("isClear")
    var isClear = false

    var rate = 0
    var statusCount = 0
    var remainGridCount = 0

    @JsonProperty("isLocked")
    var isLocked = false
}
