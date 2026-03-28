package icu.samnyan.aqua.sega.allnet

import ext.async
import icu.samnyan.aqua.net.db.AquaNetUser
import jakarta.persistence.*
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import java.security.SecureRandom

/**
 * This is a one-to-many mapping of keychip to session token.
 */
@Entity
@Table(name = "allnet_keychip_sessions", indexes = [
    Index(name = "idx_last_use", columnList = "lastUse")
])
class KeychipSession(
    @ManyToOne
    @JoinColumn(name = "au_id")
    var user: AquaNetUser? = null,

    @Column(length = 4)
    val gameId: String,

    @Id
    @Column(length = 32)
    val token: String = genUrlSafeToken(32),

    @Column(nullable = false)
    var lastUse: Long = System.currentTimeMillis()
)


val urlSafeChars = ('a'..'z') + ('A'..'Z') + ('0'..'9') + listOf('-', '_', '.', '~')

fun genUrlSafeToken(length: Int): String {
    val random = SecureRandom()
    return (1..length)
        .map { urlSafeChars[random.nextInt(urlSafeChars.size)] }
        .joinToString("")
}

@Repository("KeychipSessionRepo")
interface KeychipSessionRepo : JpaRepository<KeychipSession, String> {
    fun findByToken(token: String): KeychipSession?

    @Transactional
    fun deleteAllByLastUseBefore(expire: Long)
}

@Service
class KeychipSessionService(
    val repo: KeychipSessionRepo,
    val props: AllNetProps
) {
    val logger = LoggerFactory.getLogger(KeychipSessionService::class.java)

    /**
     * Delete sessions that are older than the expire time.
     */
    @Scheduled(fixedDelayString = "\${allnet.server.keychip-ses-clean-interval}")
    suspend fun cleanup() = async {
        logger.info("!!! Keychip session cleanup !!!")
        val expire = System.currentTimeMillis() - props.keychipSesExpire
        repo.deleteAllByLastUseBefore(expire)
    }

    /**
     * Create a new session.
     */
    fun new(user: AquaNetUser?, gameId: String): KeychipSession {
        val session = KeychipSession(user = user, gameId = gameId)
        return repo.save(session)
    }

    /**
     * Find a session. If found, renew the last use time.
     */
    fun find(token: String) = repo.findByToken(token)?.apply {
        lastUse = System.currentTimeMillis()
        try {
            repo.save(this)
        } catch (_: Exception) {
            logger.error("Failed to update last use time for session $token")
        }
    }
}
