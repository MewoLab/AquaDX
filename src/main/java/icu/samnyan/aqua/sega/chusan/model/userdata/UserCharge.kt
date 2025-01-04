package icu.samnyan.aqua.sega.chusan.model.userdata

import jakarta.persistence.Entity
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import java.time.LocalDateTime

@Entity(name = "ChusanUserCharge")
@Table(name = "chusan_user_charge", uniqueConstraints = [UniqueConstraint(columnNames = ["user_id", "charge_id"])])
class UserCharge : Chu3UserEntity() {
    var chargeId = 0
    var stock = 0
    var purchaseDate: LocalDateTime? = null
    var validDate: LocalDateTime? = null
    var param1 = 0
    var param2 = 0
    var paramDate: LocalDateTime? = null
}
