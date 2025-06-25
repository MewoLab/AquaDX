package icu.samnyan.aqua.net.db

import jakarta.persistence.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.io.Serializable
import java.time.Instant

@Entity
@Table(name = "aqua_net_user_fedy")
class AquaNetUserFedy(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @Column(nullable = false)
    var createdAt: Instant = Instant.now(),

    // Linking to the AquaNetUser
    @OneToOne
    @JoinColumn(name = "auId", referencedColumnName = "auId")
    var aquaNetUser: AquaNetUser,
) : Serializable

@Repository
interface AquaNetUserFedyRepo : JpaRepository<AquaNetUserFedy, Long> {
    fun findByAquaNetUserAuId(auId: Long): AquaNetUserFedy?
    fun deleteByAquaNetUserAuId(auId: Long): Unit
}
