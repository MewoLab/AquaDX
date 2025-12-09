package icu.samnyan.aqua.net

import ext.*
import icu.samnyan.aqua.net.components.EmailProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.RestController
import java.security.MessageDigest
import icu.samnyan.aqua.net.utils.SUCCESS
import icu.samnyan.aqua.net.components.JWT
import icu.samnyan.aqua.net.db.AquaNetUser
import icu.samnyan.aqua.net.db.AquaUserServices
import icu.samnyan.aqua.net.games.mai2.Mai2Import
import icu.samnyan.aqua.net.games.ExportOptions
import icu.samnyan.aqua.sega.maimai2.handler.UploadUserPlaylogHandler as Mai2UploadUserPlaylogHandler
import icu.samnyan.aqua.sega.maimai2.handler.UpsertUserAllHandler as Mai2UpsertUserAllHandler
import icu.samnyan.aqua.net.utils.ApiException
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.support.TransactionTemplate
import icu.samnyan.aqua.sega.maimai2.model.Mai2UserDataRepo
import icu.samnyan.aqua.net.games.GenericUserDataRepo
import icu.samnyan.aqua.net.games.IUserData
import icu.samnyan.aqua.net.utils.PathProps
import icu.samnyan.aqua.sega.chusan.model.Chu3UserDataRepo
import icu.samnyan.aqua.sega.general.dao.CardRepository
import icu.samnyan.aqua.sega.general.model.Card
import icu.samnyan.aqua.sega.general.service.CardService
import icu.samnyan.aqua.sega.ongeki.OgkUserDataRepo
import icu.samnyan.aqua.sega.wacca.model.db.WcUserRepo
import org.springframework.context.ApplicationContext
import org.springframework.web.multipart.MultipartFile
import java.time.Instant
import java.util.concurrent.CompletableFuture
import kotlin.io.path.getLastModifiedTime
import kotlin.io.path.isRegularFile
import kotlin.io.path.writeBytes

@Configuration
@ConfigurationProperties(prefix = "aqua-net.fedy")
class FedyProps {
    var enabled: Boolean = false
    var key: String = ""
    var remote: String = ""
}

data class UserProfilePicture(val url: Str, val updatedAtMs: Long)
data class UserBasicInfo(
    val auId: Long, val ghostExtId: Long, val registrationTimeMs: Long,
    val username: Str, val displayName: Str, val email: Str, val passwordHash: Str, val profileBio: Str,
    val profilePicture: UserProfilePicture?,
)

private data class UserUpdatedEvent(val user: UserBasicInfo, val isNewlyCreated: Bool)
private data class CardCreatedEvent(val luid: Str, val extId: Long)
private data class CardLinkedEvent(val luid: Str, val oldExtId: Long?, val ghostExtId: Long, val migratedGames: List<Str>)
private data class CardUnlinkedEvent(val luid: Str)
private data class DataUpdatedEvent(val extId: Long, val isGhostCard: Bool, val game: Str, val removeOldData: Bool)

private data class FedyEvent(
    var userUpdated: UserUpdatedEvent? = null,
    var cardCreated: CardCreatedEvent? = null,
    var cardLinked: CardLinkedEvent? = null,
    var cardUnlinked: CardUnlinkedEvent? = null,
    var dataUpdated: DataUpdatedEvent? = null,
)

@RestController
@API("/api/v2/fedy", consumes = ["multipart/form-data"])
class Fedy(
    val jwt: JWT,
    val emailProps: EmailProperties,
    val cardRepo: CardRepository,
    val mai2UserDataRepo: Mai2UserDataRepo,
    val chu3UserDataRepo: Chu3UserDataRepo,
    val ongekiUserDataRepo: OgkUserDataRepo,
    val waccaUserDataRepo: WcUserRepo,
    val props: FedyProps,
    val paths: PathProps,
    val transactionManager: PlatformTransactionManager,
    ctx: ApplicationContext
) {
    val us by ctx.lazy<AquaUserServices>()
    val cardService by ctx.lazy<CardService>()
    val mai2Import by ctx.lazy<Mai2Import>()
    val mai2UploadUserPlaylog by ctx.lazy<Mai2UploadUserPlaylogHandler>()
    val mai2UpsertUserAll by ctx.lazy<Mai2UpsertUserAllHandler>()

    val transaction by lazy { TransactionTemplate(transactionManager) }

    private fun Str.checkKey() {
        if (!props.enabled) 403 - "Fedy is disabled"
        if (!MessageDigest.isEqual(this.toByteArray(), props.key.toByteArray())) 403 - "Invalid Key"
    }

    val suppressEvents = ThreadLocal.withInitial { false }
    private fun <T> handleFedy(key: Str, block: () -> T): T {
        val old = suppressEvents.get()
        suppressEvents.set(true)
        try {
            key.checkKey()
            return block()
        } finally { suppressEvents.set(old) }
    }

    data class FedyErr(val code: Int, val message: Str)

    data class UserPullReq(val auId: Long)
    data class UserPullRes(val user: UserBasicInfo?)
    @API("/user/pull")
    fun handleUserPull(@RH(KEY_HEADER) key: Str, @RT(REQ_PART) req: UserPullReq): UserPullRes = handleFedy(key) {
        UserPullRes(us.userRepo.findByAuId(req.auId)?.fedyBasicInfo())
    }

    data class UserLookupReq(val username: Str?, val email: Str?)
    data class UserLookupRes(val user: UserBasicInfo?)
    @API("/user/lookup")
    fun handleUserLogin(@RH(KEY_HEADER) key: Str, @RT(REQ_PART) req: UserLookupReq): UserLookupRes = handleFedy(key) {
        UserLookupRes(user =
            (req.username?.let { us.userRepo.findByUsernameIgnoreCase(it) } ?: req.email?.let {us.userRepo.findByEmailIgnoreCase(it) })
                ?.takeIf { it.emailConfirmed || !emailProps.enable }
                ?.fedyBasicInfo()
        )
    }

    data class UserRegisterReq(val username: Str, val email: Str, val password: Str)
    data class UserRegisterRes(val error: FedyErr? = null, val user: UserBasicInfo? = null)
    @API("/user/register")
    fun handleUserRegister(@RH(KEY_HEADER) key: Str, @RT(REQ_PART) req: UserRegisterReq): UserRegisterRes = handleFedy(key) {
        {
            UserRegisterRes(user = us.create(req.username, req.email, req.password, "", emailConfirmed = true).fedyBasicInfo())
        } caught { UserRegisterRes(error = it) }
    }

    data class UserUpdateReq(val auId: Long, val fields: Map<Str, Str?>?)
    data class UserUpdateRes(val error: FedyErr? = null, val user: UserBasicInfo? = null)
    @API("/user/update")
    fun handleUserUpdate(@RH(KEY_HEADER) key: Str, @RT(REQ_PART) req: UserUpdateReq, @RT(PFP_PART) pfpFile: MultipartFile?): UserUpdateRes = handleFedy(key) {
        {
            val ru = us.userRepo.findByAuId(req.auId) ?: (404 - "User not found")
            val fields = req.fields?.filterValues { it != null }?.mapValues { it.value as Str } ?: emptyMap()
            fields.forEach { (k, v) ->
                if (k == "email") { ru.email = us.validateEmail(v) }
                else us.update(ru, k, v)
            }
            pfpFile?.run {
                val mime = TIKA.detect(pfpFile.bytes).takeIf { it.startsWith("image/") } ?: (400 - "Invalid file type")
                val name = "${ru.auId}${MIMES.forName(mime)?.extension ?: ".jpg"}"
                (paths.aquaNetPortrait.path() / name).writeBytes(bytes)
                ru.profilePicture = name
            }
            us.userRepo.save(ru)
            if (fields.containsKey("pwHash") ?: false) { us.clearAllSessions(ru) }
            UserUpdateRes(user = ru.fedyBasicInfo())
        } caught { UserUpdateRes(error = it) }
    }

    private fun AquaNetUser.fedyBasicInfo() = UserBasicInfo(
        auId, ghostCard.extId, regTime,
        username, displayName, email, pwHash, profileBio ?: "",
        profilePicture
            ?.let { paths.aquaNetPortrait.path() / it }?.takeIf { it.isRegularFile() }
            ?.let { UserProfilePicture(
                url = "/uploads/net/portrait/${profilePicture}",
                updatedAtMs = it.getLastModifiedTime().toMillis()
            ) }
    )

    data class DataPullReq(val extId: Long, val game: Str, val createdAtMs: Long, val updatedAtMs: Long, val exportOptions: ExportOptions)
    data class DataPullResult(val data: Any?, val createdAtMs: Long, val updatedAtMs: Long, val isRebased: Bool)
    data class DataPullRes(val error: FedyErr? = null, val result: DataPullResult? = null)
    @API("/data/pull")
    fun handleDataPull(@RH(KEY_HEADER) key: Str, @RT(REQ_PART) req: DataPullReq): DataPullRes = handleFedy(key) {
        val card = cardRepo.findByExtId(req.extId).orElse(null)
            ?: (404 - "Card with extId ${req.extId} not found")
        val cardTimestamp = cardService.getCardTimestamp(card, req.game)
        if (cardTimestamp.updatedAt.toEpochMilli() == req.updatedAtMs) return@handleFedy DataPullRes(error = null, result = null) // No changes
        val isRebased = req.createdAtMs > 0 && cardTimestamp.createdAt.toEpochMilli() > req.createdAtMs
        val exportOptions = if (!isRebased) { req.exportOptions } else { req.exportOptions.copy(playlogAfter = null) }
        {
            DataPullRes(result = DataPullResult(data = when (req.game) {
                "mai2" -> mai2Import.export(card, exportOptions)
                else -> 406 - "Unsupported game"
            }, createdAtMs = cardTimestamp.createdAt.toEpochMilli(), updatedAtMs = cardTimestamp.updatedAt.toEpochMilli(), isRebased = isRebased))
        } caught { DataPullRes(error = it) }
    }

    data class DataPushReq(val extId: Long, val game: Str, val data: JDict, val removeOldData: Bool, val updatedAtMs: Long)
    @Suppress("UNCHECKED_CAST")
    @API("/data/push")
    fun handleDataPush(@RH(KEY_HEADER) key: Str, @RT(REQ_PART) req: DataPushReq): Any = handleFedy(key) {
        val extId = req.extId
        fun<UserData : IUserData, UserRepo : GenericUserDataRepo<UserData>> removeOldData(repo: UserRepo) {
            val oldData = repo.findByCard_ExtId(extId)
            if (oldData.isPresent) {
                log.info("Fedy: Deleting old data for $extId (${req.game})")
                repo.delete(oldData.get());
                repo.flush()
            }
        }
        val card = cardRepo.findByExtId(extId).orElse(null) ?: (404 - "Card not found")
        transaction.execute { when (req.game) {
            "mai2" -> {
                if (req.removeOldData) { removeOldData(mai2UserDataRepo) }
                val userAll = req.data["upsertUserAll"] as JDict // UserAll first, prevent using backlog
                mai2UpsertUserAll.handle(mapOf("userId" to extId, "upsertUserAll" to userAll))
                val playlogs = req.data["userPlaylogList"] as List<JDict>
                playlogs.forEach { mai2UploadUserPlaylog.handle(mapOf("userId" to extId, "userPlaylog" to it)) }
            }
            else -> 406 - "Unsupported game"
        } }
        cardService.updateCardTimestamp(card, req.game, now = Instant.ofEpochMilli(req.updatedAtMs), resetCreatedAt = req.removeOldData)
        SUCCESS
    }

    data class CardResolveReq(val luid: Str, val pairedLuid: Str?, val createIfNotFound: Bool)
    data class CardResolveRes(val extId: Long, val isGhostCard: Bool, val isNewlyCreated: Bool, val isPairedLuidDiverged: Bool)
    @API("/card/resolve")
    fun handleCardResolve(@RH(KEY_HEADER) key: Str, @RT(REQ_PART) req: CardResolveReq): CardResolveRes = handleFedy(key) {
        var card = cardService.tryLookup(req.luid)
        var isNewlyCreated = false
        if (card != null) {
            card = card.maybeGhost()
            if (!card.isGhost) isNewlyCreated = isCardFresh(card)
        } else if (req.createIfNotFound) {
            card = cardService.registerByAccessCode(req.luid, null)
            isNewlyCreated = true
            log.info("Fedy /card/resolve : Created new card ${card.id} (${card.luid})")
        }
        var isPairedLuidDiverged = false
        if (req.pairedLuid != null) {
            var pairedCard = cardService.tryLookup(req.pairedLuid)?.maybeGhost()
            if (pairedCard?.extId != card?.extId) {
                var isGhost = pairedCard?.isGhost == true
                var isNonFresh = pairedCard != null && !isCardFresh(pairedCard)
                if (isGhost || isNonFresh) isPairedLuidDiverged = true
                else if (card?.isGhost == true) {
                    // Ensure paired card is linked, if the main card is linked
                    // If the main card is not linked, there's nothing Fedy can do. It's Fedy's best effort.
                    if (pairedCard == null) { pairedCard = cardService.registerByAccessCode(req.pairedLuid, card.aquaUser) }
                    else { pairedCard.aquaUser = card.aquaUser; cardRepo.save(pairedCard) }
                    log.info("Fedy /card/resolve : Created paired card ${pairedCard.id} (${pairedCard.luid}) for user ${card.aquaUser?.auId} (${card.aquaUser?.username})")
                }
            }
        }

        CardResolveRes(
            card?.extId ?: 0,
            card?.isGhost ?: false,
            isNewlyCreated,
            isPairedLuidDiverged)
    }

    data class CardLinkReq(val auId: Long, val luid: Str)
    @API("/card/link")
    fun handleCardLink(@RH(KEY_HEADER) key: Str, @RT(REQ_PART) req: CardLinkReq): Any = handleFedy(key) {
        val ru = us.userRepo.findByAuId(req.auId) ?: (404 - "User not found")
        var card = cardService.tryLookup(req.luid)
        if (card == null) {
            card = cardService.registerByAccessCode(req.luid, ru)
            log.info("Fedy /card/link : Linked new card ${card.id} (${card.luid}) to user ${ru.auId} (${ru.username})")
        } else {
            if (card.isGhost) 400 - "Account virtual cards cannot be unlinked"
            val cu = card.aquaUser
            if (cu != null) {
                if (cu.auId == req.auId) log.info("Fedy /card/link : Existing card ${card.id} (${card.luid}) already linked to user ${ru.auId} (${ru.username})")
                else 400 - "Card linked to another user"
            } else {
                card.aquaUser = ru
                cardRepo.save(card)
                log.info("Fedy /card/link : Linked existing card ${card.id} (${card.luid}) to user ${ru.auId} (${ru.username})")
            }
        }
    }

    data class CardUnlinkReq(val auId: Long, val luid: Str)
    @API("/card/unlink")
    fun handleCardUnlink(@RH(KEY_HEADER) key: Str, @RT(REQ_PART) req: CardUnlinkReq): Any = handleFedy(key) {
        val card = cardService.tryLookup(req.luid)
        val cu = card?.aquaUser ?: return@handleFedy SUCCESS // Nothing to do

        if (cu.auId != req.auId) 400 - "Card linked to another user"
        if (card.isGhost) 400 - "Account virtual cards cannot be unlinked"

        card.aquaUser = null
        cardRepo.save(card)
        log.info("Fedy /card/unlink : Unlinked card ${card.id} (${card.luid}) from user ${cu.auId} (${cu.username})")
    }

    fun onUserUpdated(u: AquaNetUser, isNew: Bool = false) = maybeNotifyAsync(FedyEvent(userUpdated = UserUpdatedEvent(u.fedyBasicInfo(), isNew)))
    fun onCardCreated(luid: Str, extId: Long) = maybeNotifyAsync(FedyEvent(cardCreated = CardCreatedEvent(luid, extId)))
    fun onCardLinked(luid: Str, oldExtId: Long?, ghostExtId: Long, migratedGames: List<Str>) = maybeNotifyAsync(FedyEvent(cardLinked = CardLinkedEvent(luid, oldExtId, ghostExtId, migratedGames)))
    fun onCardUnlinked(luid: Str) = maybeNotifyAsync(FedyEvent(cardUnlinked = CardUnlinkedEvent(luid)))
    fun onDataUpdated(extId: Long, game: Str, removeOldData: Bool) = maybeNotifyAsync({
        val card = cardRepo.findByExtId(extId).orElse(null) ?: return@maybeNotifyAsync null // Card not found, nothing to do
        FedyEvent(dataUpdated = DataUpdatedEvent(extId, card.isGhost, game, removeOldData))
    })

    private fun maybeNotifyAsync(event: FedyEvent) = maybeNotifyAsync({ event })
    private fun maybeNotifyAsync(getEvent: () -> FedyEvent?) = if (!props.enabled && !suppressEvents.get()) {} else CompletableFuture.runAsync {
        var event: FedyEvent? = null
        try {
            event = getEvent()
            if (event == null) return@runAsync // Nothing to do
            notify(event)
        } catch (e: Exception) {
            log.error("Error handling Fedy on maybeNotifyAsync($event)", e)
        }
    }.let {}

    private fun notify(event: FedyEvent) {
        val MAX_RETRY = 3
        val body = event.toJson() ?: "{}"
        var retry = 0
        var shouldRetry = true
        while (true) {
            try {
                val response = "${props.remote.trimEnd('/')}/notify".request()
                    .header("Content-Type" to "application/json")
                    .header(KEY_HEADER to props.key)
                    .post(body)
                val statusCodeStr = response.statusCode().toString()
                val hasError = !statusCodeStr.startsWith("2")
                // Check for non-transient errors
                if (hasError) {
                    if (!statusCodeStr.startsWith("5")) { shouldRetry = false }
                    throw Exception("Failed to notify Fedy event $event with body $body, status code $statusCodeStr")
                }
                return
            } catch (e: Exception) {
                retry++
                if (retry >= MAX_RETRY || !shouldRetry) throw e
                log.error("Error notifying Fedy event $event with body $body, retrying ($retry/$MAX_RETRY)", e)
            }
        }
    }

    // Apparently existing cards could possibly be fresh and never used in any game. Treat them as new cards.
    private fun isCardFresh(c: Card): Bool {
        fun <T : IUserData> checkForGame(repo: GenericUserDataRepo<T>, card: Card): Bool = repo.findByCard(card) != null
        return when {
            checkForGame(mai2UserDataRepo, c) -> false
            checkForGame(chu3UserDataRepo, c) -> false
            checkForGame(ongekiUserDataRepo, c) -> false
            checkForGame(waccaUserDataRepo, c) -> false
            else -> true
        }
    }

    private infix fun <T> (() -> T).caught(onError: (FedyErr) -> T) =
        try { this() }
        catch (e: ApiException) { onError(FedyErr(code = e.code, message = e.message.toString())) }

    companion object
    {
        const val KEY_HEADER = "X-Fedy-Key"
        const val REQ_PART = "request"
        const val PFP_PART = "profilePicture"
        val log = logger()
    }
}
