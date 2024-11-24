package icu.samnyan.aqua.net.db

import com.fasterxml.jackson.annotation.JsonIgnore
import ext.*
import icu.samnyan.aqua.net.components.JWT
import icu.samnyan.aqua.sega.allnet.AllNetProps
import icu.samnyan.aqua.sega.allnet.KeyChipRepo
import icu.samnyan.aqua.sega.allnet.KeychipSession
import icu.samnyan.aqua.sega.general.dao.CardRepository
import icu.samnyan.aqua.sega.general.model.Card
import jakarta.persistence.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.io.Serializable
import kotlin.jvm.optionals.getOrNull
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.functions

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

interface AquaNetUserRepo : JpaRepository<AquaNetUser, Long> {
    fun findByAuId(auId: Long): AquaNetUser?
    fun findByEmailIgnoreCase(email: String): AquaNetUser?
    fun findByUsernameIgnoreCase(username: String): AquaNetUser?
    fun findByKeychip(keychip: String): AquaNetUser?
}

data class SettingField(
    val name: Str,
    val checker: KFunction<*>,
    val setter: KMutableProperty.Setter<*>,
)

/**
 * This class is a validator for user fields. It will return the parsed value if the field is valid, or
 * throw an ApiException if the field is invalid.
 */
@Service
class AquaUserServices(
    val userRepo: AquaNetUserRepo,
    val cardRepo: CardRepository,
    val hasher: PasswordEncoder,
    val keyChipRepo: KeyChipRepo,
    val allNetProps: AllNetProps,
    val jwt: JWT,
    val em: EntityManager
) {
    companion object {
        val SETTING_FIELDS = AquaUserServices::class.functions
            .filter { it.name.startsWith("check") }
            .map {
                val name = it.name.removePrefix("check").replaceFirstChar { c -> c.lowercase() }
                val prop = AquaNetUser::class.members.find { m -> m.name == name } as KMutableProperty<*>
                SettingField(name, it, prop.setter)
            }
    }

    suspend fun <T> byName(username: Str, callback: suspend (AquaNetUser) -> T) =
        async { userRepo.findByUsernameIgnoreCase(username) }?.let { callback(it) } ?: (404 - "User not found")

    suspend fun cardByName(username: Str) =
        if (username.startsWith("user")) username.substring(4).toLongOrNull()
            ?.let { cardRepo.findById(it).getOrNull() } ?: (404 - "Card not found")
        else byName(username) { it.ghostCard }

    suspend fun <T> cardByName(username: Str, callback: suspend (Card) -> T) = callback(cardByName(username))

    fun validKeychip(keychipId: Str): Bool {
        if (!allNetProps.checkKeychip) return true
        if (keychipId.isBlank()) return false
        if (userRepo.findByKeychip(keychipId) != null || keyChipRepo.existsByKeychipId(keychipId)) return true
        return false
    }

    fun checkUsername(username: Str) = username.apply {
        // Check if username is valid
        if (length < 2) 400 - "Username must be at least 2 letters"
        if (length > 32) 400 - "Username too long (max 32 letters)"
        if (contains(" ")) 400 - "Username cannot contain spaces"

        // card{id} is a reserved format
        if (startsWith("user") && substring(4).toLongOrNull() != null)
            400 - "Username cannot be 'user' + a number. This format is reserved for user IDs."

        // Check if username is within A-Za-z0-9_-~.
        find { !it.isLetterOrDigit() && it != '_' && it != '-' && it != '~' && it != '.' }?.let {
            400 - "Username cannot contain `$it`. Please only use letters (A-Z), numbers (0-9), and `_-~.` characters. You can set a display name later."
        }

        // Check if user with the same username exists
        if (userRepo.findByUsernameIgnoreCase(this) != null)
            400 - "User with username `$this` already exists"
    }

    fun checkEmail(email: Str) = email.apply {
        // Check if email is valid
        if (!isValidEmail()) 400 - "Invalid email"

        // Check if user with the same email exists
        if (userRepo.findByEmailIgnoreCase(email) != null)
            400 - "User with email `$email` already exists"
    }

    fun checkPwHash(password: Str) = password.run {
        // Validate password
        if (length < 8) 400 - "Password must be at least 8 characters"

        hasher.encode(this)
    }

    fun checkDisplayName(displayName: Str) = displayName.apply {
        // Check if display name is valid
        if (length > 32) 400 - "Display name too long (max 32 letters)"
    }

    fun checkProfileLocation(profileLocation: Str) = profileLocation.apply {
        // Check if profile location is valid
        if (length > 64) 400 - "Profile location too long (max 64 letters)"
    }

    fun checkProfileBio(profileBio: Str) = profileBio.apply {
        // Check if profile bio is valid
        if (length > 255) 400 - "Profile bio too long (max 255 letters)"
    }

    fun checkOptOutOfLeaderboard(optOutOfLeaderboard: Str) = optOutOfLeaderboard.toBoolean()
}
