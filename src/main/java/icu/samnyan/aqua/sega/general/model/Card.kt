package icu.samnyan.aqua.sega.general.model

import com.fasterxml.jackson.annotation.JsonIgnore
import icu.samnyan.aqua.net.db.AquaNetUser
import jakarta.persistence.*
import java.time.LocalDateTime

enum class CardStatus {
    NORMAL,

    // Reserved for future use
    NORMAL_RESERVED_1,
    NORMAL_RESERVED_2,
    NORMAL_RESERVED_3,
    NORMAL_RESERVED_4,
    NORMAL_RESERVED_5,
    NORMAL_RESERVED_6,
    NORMAL_RESERVED_7,
    NORMAL_RESERVED_8,
    NORMAL_RESERVED_9,

    // Deleted statuses
    OVERWRITTEN,
    DELETED,
    MIGRATED_TO_MINATO,
}

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Entity(name = "SegaCard")
@Table(name = "sega_card")
class Card(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    val id: Long = 0,

    // An external id (THIS IS UINT32!!!!)
    // IDK why samnyan used Long, Games cannot parse the full int64 value and will cast it to uint32
    @Column(name = "ext_id", unique = true, nullable = false)
    @JsonIgnore // Sensitive information
    var extId: Long = 0,

    // Access Code
    @Column(unique = true, nullable = false)
    var luid: String = "",

    @Column(name = "register_time", nullable = false)
    var registerTime: LocalDateTime = LocalDateTime.now(),

    @Column(name = "access_time", nullable = false)
    var accessTime: LocalDateTime = registerTime,

    // Defines the AquaNet user that this card is bound to
    @ManyToOne
    @JoinColumn(name = "net_user_id")
    @JsonIgnore
    var aquaUser: AquaNetUser? = null,

    // Whether the card is a ghost card
    var isGhost: Boolean = false,

    // Unfortunately some people decide to cheat and upload all perfect scores :(
    // This will not affect gameplay behavior, but will hide the user from ranking
    var rankingBanned: Boolean = false,

    @Enumerated(EnumType.ORDINAL)
    var status: CardStatus = CardStatus.NORMAL,
) {
    @Suppress("unused") // Used by serialization
    val isLinked get() = aquaUser != null

    /**
     * Returns ghost card if this card is a ghost card, otherwise returns this card
     */
    fun maybeGhost() = aquaUser?.ghostCard ?: this
}

fun Card.sensitiveInfo() = mapOf("id" to id, "extId" to extId, "luid" to luid)
