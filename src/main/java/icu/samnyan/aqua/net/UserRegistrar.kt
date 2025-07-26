package icu.samnyan.aqua.net

import ext.*
import icu.samnyan.aqua.net.components.*
import icu.samnyan.aqua.net.db.*
import icu.samnyan.aqua.net.db.AquaUserServices.Companion.SETTING_FIELDS
import icu.samnyan.aqua.net.utils.PathProps
import icu.samnyan.aqua.net.utils.SUCCESS
import icu.samnyan.aqua.sega.general.dao.CardRepository
import icu.samnyan.aqua.sega.general.model.Card
import icu.samnyan.aqua.sega.general.model.CardStatus
import icu.samnyan.aqua.sega.general.service.CardService
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.time.Instant
import java.time.LocalDateTime
import kotlin.io.path.writeBytes

@RestController
@API("/api/v2/user")
class UserRegistrar(
    val userRepo: AquaNetUserRepo,
    val hasher: PasswordEncoder,
    val turnstileService: TurnstileService,
    val emailService: EmailService,
    val geoIP: GeoIP,
    val jwt: JWT,
    val confirmationRepo: EmailConfirmationRepo,
    val resetPasswordRepo: ResetPasswordRepo,
    val cardRepo: CardRepository,
    val cardService: CardService,
    val validator: AquaUserServices,
    val emailProps: EmailProperties,
    final val paths: PathProps
) {
    val portraitPath = paths.aquaNetPortrait.path()

    companion object {
        // Random long with length 9-10
        // We chose 1e9 as the start because normal cards took 0...1e9-1
        // This is because games can only take uint32 for card ID, which is at max 10 digits (4294967295)
        const val cardExtIdStart = 1e9.toLong()
        // Actually, let's not use the UInt32 max but use signed int32 max instead, because Wacca doesn't support uint32
        // const val cardExtIdEnd = 4294967295
        // This range already gives us 1147483647 users, which is more than enough for now
        const val cardExtIdEnd = Int.MAX_VALUE.toLong()

        val log = LoggerFactory.getLogger(UserRegistrar::class.java)
    }

    @API("/register")
    @Doc("Register a new user. This will also create a ghost card for the user and send a confirmation email.", "Success message")
    suspend fun register(
        @RP username: Str, @RP email: Str, @RP password: Str, @RP turnstile: Str,
        request: HttpServletRequest
    ): Any {
        val ip = geoIP.getIP(request)
        log.info("Net: /user/register from $ip : $username")

        // Check captcha
        if (!turnstileService.validate(turnstile, ip)) 400 - "Invalid captcha"

        // GeoIP check to infer country
        val country = geoIP.getCountry(ip)

        // Create user
        val u = async { AquaNetUser(
            username = validator.checkUsername(username),
            email = validator.checkEmail(email),
            pwHash = validator.checkPwHash(password),
            regTime = millis(), lastLogin = millis(), country = country,
        ) }

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
        async {
            userRepo.save(u)
            cardRepo.save(card)
        }

        // Send confirmation email
        emailService.sendConfirmation(u)

        return mapOf("success" to true)
    }

    @API("/login")
    @Doc("Login with email/username and password. This will also check if the email is verified and send another confirmation", "JWT token")
    suspend fun login(
        @RP email: Str, @RP password: Str, @RP turnstile: Str,
        request: HttpServletRequest
    ): Any {
        // Check captcha
        val ip = geoIP.getIP(request)
        log.info("Net: /user/login from $ip : $email")
        if (!turnstileService.validate(turnstile, ip)) 400 - "Invalid captcha"

        // Treat email as email / username
        val user = async { userRepo.findByEmailIgnoreCase(email) ?: userRepo.findByUsernameIgnoreCase(email) }
            ?: (400 - "User not found")
        if (!hasher.matches(password, user.pwHash)) 400 - "Invalid password"

        if (user.ghostCard.status == CardStatus.MIGRATED_TO_MINATO) 400 - "Login not allowed: Card has been migrated to Minato."

        // Check if email is verified
        if (!user.emailConfirmed && emailProps.enable) {
            // Check if last confirmation email was sent within a minute
            val confirmations = async { confirmationRepo.findByAquaNetUserAuId(user.auId) }
            val lastConfirmation = confirmations.maxByOrNull { it.createdAt }

            if (lastConfirmation?.createdAt?.plusSeconds(60)?.isAfter(Instant.now()) == true) {
                400 - "Email not verified - STATE_0"
            }

            // Check if we have sent more than 3 confirmation emails in the last 24 hours
            if (confirmations.count { it.createdAt.plusSeconds(60 * 60 * 24).isAfter(Instant.now()) } > 3) {
                400 - "Email not verified - STATE_1"
            }

            // Send another confirmation email
            emailService.sendConfirmation(user)
            400 - "Email not verified - STATE_2"
        }

        // Generate JWT token
        val token = jwt.gen(user)

        // Set last login time
        async { userRepo.save(user.apply { lastLogin = millis() }) }
        log.info("> Login success: ${user.username} ${user.auId}")

        return mapOf("token" to token)
    }

    @API("/reset-password")
    @Doc("Reset password with a token sent through email to the user, if it exists.", "Success message") // wtf is the second param in this annotation?
    suspend fun resetPassword(
        @RP email: Str, @RP turnstile: Str,
        request: HttpServletRequest
    ) : Any {

        // Check captcha
        val ip = geoIP.getIP(request)
        log.info("Net: /user/reset-password from $ip : $email")
        if (!turnstileService.validate(turnstile, ip)) 400 - "Invalid captcha"

        // Check if user exists, treat as email / username
        val user = async { userRepo.findByEmailIgnoreCase(email) ?: userRepo.findByUsernameIgnoreCase(email) }
            ?: return SUCCESS // obviously dont tell them if the email exists or not
        
        // Check if email is verified
        if (!user.emailConfirmed && emailProps.enable) 400 - "Email not verified" // maybe similar logic to login here

        // Send a password reset email
        emailService.sendPasswordReset(user)
        
        return SUCCESS
    }

    @API("/change-password")
    @Doc("Change a user's password given a reset code", "Success message") // again have no idea what it is
    suspend fun changePassword(
        @RP token: Str, @RP password: Str,
        request: HttpServletRequest
    ) : Any {
        
        // Find the reset token
        val reset = async { resetPasswordRepo.findByToken(token) }

        // Check if the token is valid
        if (reset == null) 400 - "Invalid token"

        // Check if the token is expired
        if (reset.createdAt.plusSeconds(60 * 60 * 24).isBefore(Instant.now())) 400 - "Token expired"

        // Change the password
        async { userRepo.save(reset.aquaNetUser.apply { pwHash = validator.checkPwHash(password) }) } // how... 

        return SUCCESS
    }

    @API("/confirm-email")
    @Doc("Confirm email address with a token sent through email to the user.", "Success message")
    suspend fun confirmEmail(@RP token: Str): Any {
        log.info("Net: /user/confirm-email with token $token")

        // Find the confirmation
        val confirmation = async { confirmationRepo.findByToken(token) }

        // Check if the token is valid
        if (confirmation == null) 400 - "Invalid token"

        // Check if the token is expired
        if (confirmation.createdAt.plusSeconds(60 * 60 * 24).isBefore(Instant.now())) 400 - "Token expired"

        // Confirm the email
        async { userRepo.save(confirmation.aquaNetUser.apply { emailConfirmed = true }) }

        return SUCCESS
    }

    @API("/me")
    @Doc("Get the information of the current logged-in user.", "User information")
    suspend fun getUser(@RP token: Str) = jwt.auth(token)

    @API("/user-info")
    @Doc("Get the information of a user by username.", "User information")
    fun getUserInfo(@RP username: Str) =
        userRepo.findByUsernameIgnoreCase(username)?.publicFields ?: (404 - "User not found")

    @API("/setting")
    @Doc("Validate and set a user setting field.", "Success message")
    suspend fun setting(@RP token: Str, @RP key: Str, @RP value: Str) = jwt.auth(token) { u ->
        // Check if the key is a settable field
        val field = SETTING_FIELDS.find { it.name == key } ?: (400 - "Invalid setting")

        async {
            // Set the validated field
            field.setter.call(u, field.checker.call(validator, value))

            // Save the user
            userRepo.save(u)
        }

        SUCCESS
    }

    val keychipRange = 1e9.toULong()..1e10.toULong() - 1UL

    @API("/keychip")
    @Doc("Get a Keychip ID so that the user can connect to the server.", "Success message")
    suspend fun setupConnection(@RP token: Str) = jwt.auth(token) { u ->
        u.keychip?.let { return mapOf("keychip" to it) }
        log.info("Net: /user/keychip setup: ${u.auId} for ${u.username}")

        // Generate a keychip id with 10 digits (e.g. A1234567890)
        var new = "A" + keychipRange.random()
        while (async { userRepo.findByKeychip(new) != null }) new = "A" + keychipRange.random()
        async { userRepo.save(u.apply { keychip = new }) }

        mapOf("keychip" to new)
    }

    @API("/upload-pfp", consumes = ["multipart/form-data"])
    @Doc("Upload a profile picture for the user.", "Success message")
    suspend fun uploadPfp(@RP token: Str, @RP file: MultipartFile) = jwt.auth(token) { u ->
        // Processing the image would lead to many open factors for attack
        // (e.g. the JFIF Pixel Flood attack that ImageIO is vulnerable to)
        // So we check file magic, then store the image without any processing
        val bytes = file.bytes
        val mime = TIKA.detect(bytes) ?: (400 - "Invalid file type")

        // Check if the file is an image
        if (!mime.startsWith("image/")) 400 - "Invalid file type"

        // Save the image
        val name = "${u.auId}${MIMES.forName(mime)?.extension ?: ".jpg"}"
        async {
            (portraitPath / name).writeBytes(bytes)
            userRepo.save(u.apply { profilePicture = name })
        }

        SUCCESS
    }
}
