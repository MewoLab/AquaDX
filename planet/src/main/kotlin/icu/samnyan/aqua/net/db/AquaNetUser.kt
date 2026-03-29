package icu.samnyan.aqua.net.db

import ext.*
import icu.samnyan.aqua.net.UserRegistrar.Companion.cardExtIdEnd
import icu.samnyan.aqua.net.UserRegistrar.Companion.cardExtIdStart
import icu.samnyan.aqua.net.components.JWT
import icu.samnyan.aqua.sega.allnet.AllNetProps
import icu.samnyan.aqua.sega.allnet.KeyChipRepo
import icu.samnyan.aqua.sega.allnet.KeychipSession
import icu.samnyan.aqua.sega.general.GameMusicPopularity
import icu.samnyan.aqua.sega.general.dao.CardRepository
import icu.samnyan.aqua.sega.general.model.Card
import icu.samnyan.aqua.sega.general.service.CardService
import jakarta.persistence.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.functions

// Entity AquaNetUser and SettingField are now in :shared

interface AquaNetUserRepo : JpaRepository<AquaNetUser, Long> {
    fun findByAuId(auId: Long): AquaNetUser?
    fun findByEmailIgnoreCase(email: String): AquaNetUser?
    fun findByUsernameIgnoreCase(username: String): AquaNetUser?
    fun findByKeychip(keychip: String): AquaNetUser?
    fun findByGhostCardExtId(extId: Long): AquaNetUser?
}

/**
 * User services (formerly inside AquaNetUser.kt in the monolithic setup).
 */
@Service
class AquaUserServices(
    val userRepo: AquaNetUserRepo,
    val cardRepo: CardRepository,
    val hasher: PasswordEncoder,
    val keyChipRepo: KeyChipRepo,
    val allNetProps: AllNetProps,
    val jwt: JWT,
    val em: EntityManager,
    val pop: GameMusicPopularity,
    val cardService: CardService,
    val sessionRepo: SessionTokenRepo,
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

    fun create(username: Str, email: Str, password: Str, country: Str, emailConfirmed: Boolean = false): AquaNetUser {
        // Create user
        val u = AquaNetUser(
            username = checkUsername(username),
            email = validateEmail(email),
            pwHash = checkPwHash(password),
            regTime = millis(), lastLogin = millis(), country = country,
            emailConfirmed = emailConfirmed
        )

        // Create a ghost card
        val card = Card().apply {
            extId = cardService.randExtID(cardExtIdStart, cardExtIdEnd)
            luid = extId.toString()
            registerTime = LocalDateTime.now()
            accessTime = registerTime
            aquaUser = u
            isGhost = true
        }
        u.ghostCard = card

        // Save the user
        userRepo.save(u)
        cardRepo.save(card)

        return u
    }

    fun update(user: AquaNetUser, key: Str, value: Str) {
        // Check if the key is a settable field
        val field = SETTING_FIELDS.find { it.name == key } ?: (400 - "Invalid setting")
        // Set the validated field
        field.setter.call(user, field.checker.call(this, value))
    }

    fun clearAllSessions(user: AquaNetUser) = sessionRepo.deleteAll(sessionRepo.findByAquaNetUserAuId(user.auId))

    suspend fun <T> byName(username: Str, callback: suspend (AquaNetUser) -> T) =
        async { userRepo.findByUsernameIgnoreCase(username) }?.let { callback(it) } ?: (404 - "User not found")

    suspend fun cardByName(username: Str) =
        if (username.startsWith("user")) username.substring(4).toLongOrNull()
            ?.let { cardRepo.findById(it)() } ?: (404 - "Card not found")
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

    fun validateEmail(email: Str) = email.apply {
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
