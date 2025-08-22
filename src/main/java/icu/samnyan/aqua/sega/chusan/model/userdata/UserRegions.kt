package icu.samnyan.aqua.sega.chusan.model.userdata

import jakarta.persistence.Entity
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import java.time.LocalDate

@Entity(name = "ChusanUserRegions")
@Table(name = "chusan_user_regions", uniqueConstraints = [UniqueConstraint(columnNames = ["user_id", "region_id"])])
class UserRegions : Chu3UserEntity() {
    var regionId = 0
    var playCount = 1
    var created: String = LocalDate.now().toString()
}
