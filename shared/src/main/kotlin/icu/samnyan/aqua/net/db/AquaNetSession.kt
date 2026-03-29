package icu.samnyan.aqua.net.db

import jakarta.persistence.*
import java.io.Serializable
import java.time.Instant
import java.util.*

fun getTokenExpiry() = Instant.now().plusSeconds(7 * 86400)

@Entity
@Table(name = "aqua_net_session")
class SessionToken(
    @Id
    @Column(nullable = false)
    var token: String = UUID.randomUUID().toString(),

    // Token creation time
    @Column(nullable = false)
    var expiry: Instant = getTokenExpiry(),

    @ManyToOne
    @JoinColumn(name = "auId", referencedColumnName = "auId")
    var aquaNetUser: AquaNetUser = AquaNetUser()
) : Serializable
