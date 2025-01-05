package icu.samnyan.aqua.sega.chusan.model.userdata

import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity(name = "ChusanUserCardPrintState")
@Table(name = "chusan_user_print_state")
class UserCardPrintState(
    var hasCompleted: Boolean = false,
    var limitDate: LocalDateTime = LocalDateTime.now(),
    var placeId: Int = 0,
    var cardId: Int = 0,
    var gachaId: Int = 0
) : Chu3UserEntity()

