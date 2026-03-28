package icu.samnyan.aqua.net.db

import jakarta.persistence.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.io.Serializable
import java.time.Instant

@Entity
@Table(name = "aqua_net_email_reset_password")
class ResetPassword(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @Column(nullable = false)
    var token: String = "",

    // Token creation time
    @Column(nullable = false)
    var createdAt: Instant = Instant.now(),

    // Linking to the AquaNetUser
    @ManyToOne
    @JoinColumn(name = "auId", referencedColumnName = "auId")
    var aquaNetUser: AquaNetUser = AquaNetUser()
) : Serializable

@Repository
interface ResetPasswordRepo : JpaRepository<ResetPassword, Long> {
    fun findByToken(token: String): ResetPassword?
    fun findByAquaNetUserAuId(auId: Long): List<ResetPassword>
}