package icu.samnyan.aqua.sega.general.model

import icu.samnyan.aqua.net.games.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "sega_card_timestamp")
class CardTimestamp : BaseEntity() {
    @Column(name = "card_id")
    var cardId: Long = 0

    @Column(name = "game_id")
    var gameId: String = ""

    @Column(name = "created_at")
    var createdAt: Long = 0

    @Column(name = "updated_at")
    var updatedAt: Long = 0
}
