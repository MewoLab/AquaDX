package aquadx.sega.allnet

import aquadx.net.db.AquaNetUser
import jakarta.persistence.*
import java.security.SecureRandom

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
