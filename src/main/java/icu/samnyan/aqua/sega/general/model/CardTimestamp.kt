package icu.samnyan.aqua.sega.general.model

import ext.Str
import jakarta.persistence.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.Instant

@Entity(name = "SegaCardTimestamp")
@Table(name = "sega_card_timestamp")
class CardTimestamp(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @Column(nullable = false)
    var createdAt: Instant = Instant.now(),

    @Column(nullable = false)
    var updatedAt: Instant = Instant.now(),

    var game: Str,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id")
    var card: Card? = null,
)

@Repository
interface CardTimestampRepo : JpaRepository<CardTimestamp, Long> {
    fun findByCardIdAndGame(cardId: Long, game: Str): CardTimestamp?
}
