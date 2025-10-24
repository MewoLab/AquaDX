package icu.samnyan.aqua.net.db

import jakarta.persistence.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
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

    // Linking to the AquaNetUser
    @ManyToOne
    @JoinColumn(name = "auId", referencedColumnName = "auId")
    var aquaNetUser: AquaNetUser = AquaNetUser()
) : Serializable

@Repository
interface SessionTokenRepo : JpaRepository<SessionToken, String> {
    fun findByToken(token: String): SessionToken?
    fun findByAquaNetUserAuId(auId: Long): List<SessionToken>
}
