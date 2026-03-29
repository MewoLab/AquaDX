package aquadx.net.db

import com.fasterxml.jackson.annotation.JsonIgnore
import ext.*
import aquadx.sega.allnet.KeychipSession
import aquadx.sega.general.model.Card
import jakarta.persistence.*
import java.io.Serializable
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty

@Entity
class AquaNetUser(
    @JsonIgnore
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var auId: Long = 0,

    @Column(nullable = false, unique = true, length = 32)
    var username: String = "",

    // Login credentials
    @Column(nullable = false, unique = true)
    var email: String = "",

    @JsonIgnore
    @Column(nullable = false)
    var pwHash: String = "",

    @Column(nullable = true, length = 32)
    var displayName: String = "",

    // Country code at most 3 characters
    @Column(length = 3)
    var country: String = "",

    // Region code at most 2 characters
    @Column(length = 2)
    var region: String = "",

    // Last login time
    var lastLogin: Long = 0L,

    // Registration time
    var regTime: Long = 0L,

    // Profile fields
    var profileLocation: String? = "",
    var profileBio: String? = "",
    var profilePicture: String? = "",
    var optOutOfLeaderboard: Boolean = false,

    // Email confirmation
    var emailConfirmed: Boolean = false,

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "ghostCard", unique = true, nullable = false)
    var ghostCard: Card = Card(),

    // One user can have multiple cards
    @OneToMany(mappedBy = "aquaUser", cascade = [CascadeType.ALL])
    var cards: MutableList<Card> = mutableListOf(),

    // Each user can have one keychip (if the user owns a cabinet)
    @JsonIgnore
    @Column(nullable = true, length = 32, unique = true)
    var keychip: Str? = null,

    // Each user's keychip can have multiple sessions
    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL])
    var keychipSessions: MutableList<KeychipSession> = mutableListOf(),

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "gameOptions", unique = true, nullable = true)
    var gameOptions: AquaGameOptions? = null,
) : Serializable {
    val computedName get() = displayName.ifEmpty { username }

    val publicFields get() = mapOf(
        "username" to username,
        "displayName" to displayName,
        "country" to country,
        "regTime" to regTime,
        "profileLocation" to profileLocation,
        "profileBio" to profileBio,
        "profilePicture" to profilePicture,
    )
}

data class SettingField(
    val name: Str,
    val checker: KFunction<*>,
    val setter: KMutableProperty.Setter<*>,
)
