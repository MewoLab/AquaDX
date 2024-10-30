package icu.samnyan.aqua.sega.maimai2.model

import com.fasterxml.jackson.annotation.JsonIgnore
import icu.samnyan.aqua.net.games.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import lombok.Data
import java.time.LocalDateTime

@Data @Entity
@Table(name = "maimai2_game_event")
class Mai2GameEvent : BaseEntity() {
    @JsonIgnore(false)
    override var id = 0L

    var type = 0
    var startDate: String? = null
    var endDate: String? = null

    @JsonIgnore
    var enable = false
}

@Data @Entity
@Table(name = "maimai2_game_charge")
class Mai2GameCharge : BaseEntity() {
    @Column(unique = true)
    var chargeId = 0L
    val orderId = 0L
    val price = 0
    val startDate: String? = null
    val endDate: String? = null
}

@Data @Entity
@Table(name = "maimai2_game_selling_card")
class Mai2GameSellingCard : BaseEntity() {
    var cardId = 0L
    var startDate: String? = null
    var endDate: String? = null
    var noticeStartDate: String? = null
    var noticeEndDate: String? = null
}
