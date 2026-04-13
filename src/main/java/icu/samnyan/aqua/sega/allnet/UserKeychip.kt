package icu.samnyan.aqua.sega.allnet

import icu.samnyan.aqua.net.db.AquaNetUser
import jakarta.persistence.*
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Entity
@Table(name = "user_keychip")
class UserKeychip(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne
    @JoinColumn(name = "au_id", nullable = false)
    var user: AquaNetUser,

    @Column(unique = true, nullable = false, length = 32)
    val keychipId: String,
)

@Repository
interface UserKeychipRepo : JpaRepository<UserKeychip, Long> {
    fun findByKeychipId(keychipId: String): UserKeychip?
    fun findByKeychipIdStartingWith(keychipIdPrefix: String): UserKeychip?
    fun existsByKeychipId(keychipId: String): Boolean
    fun findAllByUserAuId(auId: Long): List<UserKeychip>

    @Transactional
    fun deleteByKeychipIdAndUserAuId(keychipId: String, auId: Long): Long
}
