package icu.samnyan.aqua.net

import ext.*
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.RestController
import java.security.MessageDigest
import icu.samnyan.aqua.net.utils.SUCCESS
import icu.samnyan.aqua.net.components.JWT
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
import icu.samnyan.aqua.sega.general.dao.CardRepository
import java.util.concurrent.CompletableFuture

@Configuration
@ConfigurationProperties(prefix = "aqua-net.fedy")
class FedyProps {
    var enabled: Boolean = false
    var key: String = ""
    var remote: String = ""
}

private data class CardCreatedEvent(val luid: Str, val extId: Long)
private data class CardLinkedEvent(val luid: Str, val oldExtId: Long?, val extId: Long, val migratedGames: List<Str>)
private data class CardUnlinkedEvent(val luid: Str)
private data class DataUpdatedEvent(val extId: Long, val game: Str, val removeOldData: Bool)

private data class FedyEvent(
    var cardCreated: CardCreatedEvent? = null,
    var cardLinked: CardLinkedEvent? = null,
    var cardUnlinked: CardUnlinkedEvent? = null,
    var dataUpdated: DataUpdatedEvent? = null,
)

@RestController
@API("/api/v2/fedy")
class Fedy(
    val jwt: JWT,
    val cardRepo: CardRepository,
    val mai2Import: Mai2Import,
    val mai2UserDataRepo: Mai2UserDataRepo,
    val mai2UploadUserPlaylog: Mai2UploadUserPlaylogHandler,
    val mai2UpsertUserAll: Mai2UpsertUserAllHandler,
    val props: FedyProps,
    val transactionManager: PlatformTransactionManager
) {
    val transaction by lazy { TransactionTemplate(transactionManager) }

    private fun Str.checkKey() {
        if (!props.enabled) 403 - "Fedy is disabled"
        if (!MessageDigest.isEqual(this.toByteArray(), props.key.toByteArray())) 403 - "Invalid Key"
    }

    data class PullReq(val extId: Long, val game: Str, val exportOptions: ExportOptions)
    @API("/pull")
    fun handlePull(@RH(KEY_HEADER) key: Str, @RB req: PullReq): Any {
        key.checkKey()
        val card = cardRepo.findByExtId(req.extId).orElse(null)
            ?: (404 - "Card with extId ${req.extId} not found")
        fun catched(block: () -> Any) =
            try { mapOf("result" to block()) }
            catch (e: ApiException) { mapOf("error" to mapOf("code" to e.code, "message" to e.message.toString())) }
        return when (req.game) {
            "mai2" -> catched { mai2Import.export(card, req.exportOptions) }
            else -> 406 - "Unsupported game"
        }
    }

    data class PushReq(val extId: Long, val game: Str, val data: JDict, val removeOldData: Bool)
    @Suppress("UNCHECKED_CAST")
    @API("/push")
    fun handlePush(@RH(KEY_HEADER) key: Str, @RB req: PushReq): Any {
        key.checkKey()
        val extId = req.extId
        fun<UserData : IUserData, UserRepo : GenericUserDataRepo<UserData>> removeOldData(repo: UserRepo) {
            val oldData = repo.findByCard_ExtId(extId)
            if (oldData.isPresent) {
                log.info("Fedy: Deleting old data for $extId (${req.game})")
                repo.delete(oldData.get());
                repo.flush()
            }
        }
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

        return SUCCESS
    }

    fun onCardCreated(luid: Str, extId: Long) = maybeNotifyAsync(FedyEvent(cardCreated = CardCreatedEvent(luid, extId)))
    fun onCardLinked(luid: Str, oldExtId: Long?, extId: Long, migratedGames: List<Str>) = maybeNotifyAsync(FedyEvent(cardLinked = CardLinkedEvent(luid, oldExtId, extId, migratedGames)))
    fun onCardUnlinked(luid: Str) = maybeNotifyAsync(FedyEvent(cardUnlinked = CardUnlinkedEvent(luid)))
    fun onDataUpdated(extId: Long, game: Str, removeOldData: Bool) = maybeNotifyAsync(FedyEvent(dataUpdated = DataUpdatedEvent(extId, game, removeOldData)))

    @Suppress("IMPLICIT_CAST_TO_ANY")
    private fun maybeNotifyAsync(event: FedyEvent) = if (!props.enabled) {} else CompletableFuture.runAsync { try {
        notify(event)
    } catch (e: Exception) {
        log.error("Error handling Fedy on maybeNotifyAsync($event)", e)
    } }

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

    companion object
    {
        const val KEY_HEADER = "X-Fedy-Key"
        val log = logger()

        fun getGameName(gameId: Str) = when (gameId) {
            "mai2" -> "mai2"
            "SDEZ" -> "mai2"
            "chu3" -> "chu3"
            "SDHD" -> "chu3"
            "ongeki" -> "mu3"
            "SDDT" -> "mu3"
            "wacca" -> "wacca"
            "SDFE" -> "wacca"
            else -> null // Not supported
        }
    }
}
