package icu.samnyan.aqua.net

import ext.*
import icu.samnyan.aqua.net.components.*
import icu.samnyan.aqua.net.db.AquaNetUser
import icu.samnyan.aqua.net.db.AquaNetUserRepo
import icu.samnyan.aqua.net.db.AquaUserServices
import icu.samnyan.aqua.net.db.EmailConfirmationRepo
import icu.samnyan.aqua.net.db.ResetPasswordRepo
import icu.samnyan.aqua.net.utils.AquaNetProps
import icu.samnyan.aqua.net.utils.PathProps
import icu.samnyan.aqua.net.utils.SUCCESS
import icu.samnyan.aqua.sega.allnet.UserKeychip
import icu.samnyan.aqua.sega.allnet.UserKeychipRepo
import icu.samnyan.aqua.sega.chusan.model.Chu3Repos
import icu.samnyan.aqua.sega.diva.DivaRepos
import icu.samnyan.aqua.sega.diva.model.db.userdata.PlayerProfile
import icu.samnyan.aqua.sega.general.dao.CardRepository
import icu.samnyan.aqua.sega.general.model.Card
import icu.samnyan.aqua.sega.general.model.CardStatus
import icu.samnyan.aqua.sega.maimai2.model.Mai2Repos
import icu.samnyan.aqua.sega.ongeki.OngekiUserRepos
import icu.samnyan.aqua.sega.wacca.model.db.WaccaRepos
import jakarta.servlet.http.HttpServletRequest
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Path
import java.time.Instant
import kotlin.io.path.Path
import kotlin.io.path.deleteIfExists
import kotlin.io.path.exists
import kotlin.io.path.name
import kotlin.io.path.writeBytes

@RestController
@API("/api/v2/user")
class UserRegistrar(
    val userRepo: AquaNetUserRepo,
    val hasher: PasswordEncoder,
    val turnstileService: TurnstileService,
    val emailService: EmailService,
    val fedy: Fedy,
    val geoIP: GeoIP,
    val jwt: JWT,
    val confirmationRepo: EmailConfirmationRepo,
    val resetPasswordRepo: ResetPasswordRepo,
    val cardRepo: CardRepository,
    val validator: AquaUserServices,
    val emailProps: EmailProperties,
    val userKeychipRepo: UserKeychipRepo,
    val aquaNetProps: AquaNetProps,
    final val paths: PathProps,
    val accountDeletion: AccountDeletionService,
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
        val u = async { validator.create(username, email, password, country) }

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
    @Doc("Reset password with a token sent through email to the user, if it exists.", "Success message")
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
        if (!user.emailConfirmed && emailProps.enable) 400 - "Email not verified"

        val resets = async { resetPasswordRepo.findByAquaNetUserAuId(user.auId) }
        val lastReset = resets.maxByOrNull { it.createdAt }

        if (lastReset?.createdAt?.plusSeconds(60)?.isAfter(Instant.now()) == true) {
            400 - "Reset request rejected - STATE_0"
        }

        // Check if we have sent more than 3 confirmation emails in the last 24 hours
        if (resets.count { it.createdAt.plusSeconds(60 * 60 * 24).isAfter(Instant.now()) } > 3) {
            400 - "Reset request rejected - STATE_1"
        }

        // Send a password reset email
        emailService.sendPasswordReset(user)

        return SUCCESS
    }

    @API("/change-password")
    @Doc("Change a user's password given a reset code", "Success message")
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
        val u = reset.aquaNetUser
        async { userRepo.save(u.apply { pwHash = validator.checkPwHash(password) }) }
        fedy.onUserUpdated(u)

        // Remove the token from the list
        resetPasswordRepo.delete(reset)

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

        // Check if the email is already confirmed
        val u = confirmation.aquaNetUser
        if (u.emailConfirmed) 400 - "Email already confirmed"

        // Confirm the email
        async { userRepo.save(u.apply { emailConfirmed = true }) }
        fedy.onUserUpdated(u, isNew = true)

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
        async {
            validator.update(u, key, value)

            // Save the user
            userRepo.save(u)

            // Clear all tokens if changing password
            if (key == "pwHash") validator.clearAllSessions(u)
        }
        fedy.onUserUpdated(u)

        SUCCESS
    }

    val keychipPattern = Regex("^([A-Z\\d]{4}-[A-Z\\d]{11}|[A-Z\\d]{15})$")
    val keychipRange = 1e9.toULong()..1e10.toULong() - 1UL

    private fun generateKeychipId(): String {
        // 1337 is retained for backwards compatibility, it's no longer required for cabinet keychips
        var keychip = "A" + keychipRange.random() + "1337"
        while ( userKeychipRepo.existsByKeychipId(keychip) )
            keychip = "A" + keychipRange.random() + "1337"
        return keychip
    }

    private fun ensureCanModifyKeychips(u: AquaNetUser) {
        if (!u.canModifyKeychips) 403 - "You don't have permission to modify keychips"
    }
    private fun validateCustomKeychip(keychipId: Str): Str {
        val raw = keychipId.trim().uppercase()

        if (!keychipPattern.matches(raw))
            400 - "Invalid keychip format. Expected 11 or 15 characters (with optional dash)"

        return raw.replace("-", "")
    }

    @API("/keychip")
    @Doc("List all keychip IDs associated with the current user's account.", "List of keychip IDs")
    suspend fun listKeychips(@RP token: Str) = jwt.auth(token) { u ->
        val keychips = async { userKeychipRepo.findAllByUserAuId(u.auId) }
        if (keychips.isEmpty() && !u.canModifyKeychips) {
            val keychip = UserKeychip(0, u, generateKeychipId())
            userKeychipRepo.save(keychip)
            log.info("Net: Assigned keychip ${keychip.keychipId} to ${u.auId}")
        }
        mapOf("keychips" to keychips.map { it.keychipId })
    }

    @API("/keychip/add")
    @Doc("Add a custom keychip ID for the user.", "The newly added keychip ID")
    suspend fun addKeychip(@RP token: Str, @RP keychipId: Str) = jwt.auth(token) { u ->
        ensureCanModifyKeychips(u)
        log.info("Net: /user/keychip/add: ${u.auId} for ${u.username}")
        val validated = validateCustomKeychip(keychipId)

        if (async { userKeychipRepo.existsByKeychipId(validated) })
            400 - "Keychip already exists"

        if (userKeychipRepo.findAllByUserAuId(u.auId).size >= aquaNetProps.keychipLimit)
            400 - "Exceeds maximum keychip count"

        async { userKeychipRepo.save(UserKeychip(user = u, keychipId = validated)) }
        mapOf("keychipId" to validated)
    }

    @API("/keychip/delete")
    @Doc("Remove a keychip ID from the user's account.", "Success message")
    suspend fun deleteKeychip(@RP token: Str, @RP keychipId: Str) = jwt.auth(token) { u ->
        ensureCanModifyKeychips(u)
        val deleted = async { userKeychipRepo.deleteByKeychipIdAndUserAuId(keychipId, u.auId) }
        if (deleted == 0L) 404 - "Keychip not found"
        SUCCESS
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
        fedy.onUserUpdated(u)

        SUCCESS
    }

    @API("/change-region")
    @Doc("Change the region of the user.", "Success message")
    suspend fun changeRegion(@RP token: Str, @RP regionId: Str) = jwt.auth(token) { u ->
        // Check if the region is valid (between 1 and 47)
        val r = regionId.toIntOrNull() ?: (400 - "Invalid region")
        if (r !in 1..47) 400 - "Invalid region"
        async {
	        userRepo.save(u.apply { region = r.toString() })
        }

        SUCCESS
    }

    @API("/delete-account")
    @Doc("Permanently delete the current user's account and all associated game data.", "Success message")
    fun deleteAccount(@RP token: Str) = jwt.auth(token) { user ->
        val cleanup = accountDeletion.deleteDatabaseData(user.auId)
        accountDeletion.deleteUploadedFiles(cleanup)
        SUCCESS
    }
}

data class AccountFileCleanup(
    val profilePicture: String?,
    val gameUserId: Long,
    val divaScreenshots: List<String>,
)

@Service
class AccountDeletionService(
    val userRepo: AquaNetUserRepo,
    val cardRepo: CardRepository,
    val mai2: Mai2Repos,
    val chu3: Chu3Repos,
    val ongeki: OngekiUserRepos,
    val wacca: WaccaRepos,
    val diva: DivaRepos,
    val paths: PathProps,
) {
    companion object {
        val log = logger()
    }

    @Transactional
    fun deleteDatabaseData(auId: Long): AccountFileCleanup {
        val user = userRepo.findByAuId(auId) ?: (404 - "User not found")
        val ghostCard = user.ghostCard
        val divaScreenshots = deleteDiva(ghostCard)

        mai2.userData.findByCard(ghostCard)?.let { mai2.userData.delete(it) }
        chu3.userData.findByCard(ghostCard)?.let { chu3.userData.delete(it) }
        ongeki.data.findByCard(ghostCard)?.let { ongeki.data.delete(it) }
        wacca.user.findByCard(ghostCard)?.let { wacca.user.delete(it) }
        chu3.userLoginBonus.deleteAll(chu3.userLoginBonus.findByUser(ghostCard.extId.toInt()))

        // Keep Hibernate's managed state consistent with the database-level ON DELETE SET NULL.
        // Hibernate 7 rejects the flush if a managed card still references the deleted user.
        val linkedCards = cardRepo.findAllByAquaUserAuId(auId)
        linkedCards.forEach { it.aquaUser = null }
        cardRepo.saveAllAndFlush(linkedCards)

        userRepo.delete(user)
        userRepo.flush()
        cardRepo.delete(ghostCard)
        cardRepo.flush()

        log.info("Deleted account and game data for user $auId")
        return AccountFileCleanup(user.profilePicture, ghostCard.extId, divaScreenshots)
    }

    private fun deleteDiva(card: Card): List<String> {
        val profile: PlayerProfile = diva.profile.findByPdId(card.extId).orElse(null) ?: return emptyList()
        val screenshots = diva.screenShot.findByPdId(profile)

        diva.profile.delete(profile)

        return screenshots.map { it.fileName }
    }

    fun deleteUploadedFiles(cleanup: AccountFileCleanup) {
        try {
            cleanup.profilePicture
                ?.takeIf { it.isNotBlank() && Path(it).name == it }
                ?.let { (Path(paths.aquaNetPortrait) / it).deleteIfExists() }

            deleteFilesStartingWith(Path(paths.mai2Portrait), "${cleanup.gameUserId}-")
            deleteFilesStartingWith(Path(paths.mai2Plays), "${cleanup.gameUserId}-")
            deleteFilesStartingWith(Path("data/tmp"), "${cleanup.gameUserId}-")
            cleanup.divaScreenshots
                .filter { Path(it).name == it }
                .forEach { (Path("data") / it).deleteIfExists() }
        } catch (e: Exception) {
            log.error("Failed to delete one or more uploaded files for deleted user ${cleanup.gameUserId}", e)
        }
    }

    private fun deleteFilesStartingWith(directory: Path, prefix: String) {
        if (!directory.exists()) return
        Files.list(directory).use { files ->
            files.filter { it.fileName.toString().startsWith(prefix) }
                .forEach { it.deleteIfExists() }
        }
    }
}
