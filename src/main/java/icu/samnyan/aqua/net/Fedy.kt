package icu.samnyan.aqua.net

import ext.*
import icu.samnyan.aqua.sega.general.service.CardService
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.RestController
import java.security.MessageDigest
import icu.samnyan.aqua.net.db.AquaNetUserRepo
import icu.samnyan.aqua.net.db.AquaNetUserFedyRepo
import icu.samnyan.aqua.net.utils.SUCCESS
import icu.samnyan.aqua.net.components.JWT
import icu.samnyan.aqua.net.db.AquaNetUserFedy
import icu.samnyan.aqua.net.db.AquaNetUser
import icu.samnyan.aqua.net.games.ImportController
import icu.samnyan.aqua.net.games.mai2.Mai2Import
import icu.samnyan.aqua.net.games.ExportOptions
import icu.samnyan.aqua.sega.maimai2.handler.UploadUserPlaylogHandler as Mai2UploadUserPlaylogHandler
import icu.samnyan.aqua.sega.maimai2.handler.UpsertUserAllHandler as Mai2UpsertUserAllHandler
import icu.samnyan.aqua.net.utils.ApiException
import java.util.Arrays
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.support.TransactionTemplate
import icu.samnyan.aqua.sega.maimai2.model.Mai2UserDataRepo
import icu.samnyan.aqua.net.games.GenericUserDataRepo
import icu.samnyan.aqua.net.games.IUserData

@Configuration
@ConfigurationProperties(prefix = "aqua-net.fedy")
class FedyProps {
    var enabled: Boolean = false
    var key: String = ""
    var remote: String = ""
}

enum class FedyEvent {
    Linked,
    Unlinked,
    Upserted,
    Imported,
}

@RestController
@ConditionalOnProperty("aqua-net.fedy.enabled", havingValue = "true")
@API("/api/v2/fedy")
class Fedy(
    val jwt: JWT,
    val userRepo: AquaNetUserRepo,
    val userFedyRepo: AquaNetUserFedyRepo,
    val mai2Import: Mai2Import,
    val mai2UserDataRepo: Mai2UserDataRepo,
    val mai2UploadUserPlaylog: Mai2UploadUserPlaylogHandler,
    val mai2UpsertUserAll: Mai2UpsertUserAllHandler,
    val props: FedyProps,
    val transactionManager: PlatformTransactionManager
) {
    val transaction by lazy { TransactionTemplate(transactionManager) }

    private fun Str.checkKey() {
        if (!MessageDigest.isEqual(this.toByteArray(), props.key.toByteArray())) 403 - "Invalid Key"
    }

    @API("/status")
    fun handleStatus(@RP token: Str): Any {
        val user = jwt.auth(token)
        val userFedy = userFedyRepo.findByAquaNetUserAuId(user.auId)
        return mapOf("linkedAt" to (userFedy?.createdAt?.toEpochMilli() ?: 0))
    }

    @API("/link")
    fun handleLink(@RP token: Str, @RP nonce: Str): Any {
        val user = jwt.auth(token)

        if (userFedyRepo.findByAquaNetUserAuId(user.auId) != null) 412 - "User already linked"
        val userFedy = AquaNetUserFedy(aquaNetUser = user)
        userFedyRepo.save(userFedy)

        notifyRemote(FedyEvent.Linked, mapOf("auId" to user.auId, "nonce" to nonce))
        return mapOf("linkedAt" to userFedy.createdAt.toEpochMilli())
    }

    @API("/unlink")
    fun handleUnlink(@RP token: Str): Any {
        val user = jwt.auth(token)

        val userFedy = userFedyRepo.findByAquaNetUserAuId(user.auId) ?: 412 - "User not linked"
        userFedyRepo.delete(userFedy)

        notifyRemote(FedyEvent.Unlinked, mapOf("auId" to user.auId))
        return SUCCESS
    }

    private fun ensureUser(auId: Long): AquaNetUser {
        val userFedy = userFedyRepo.findByAquaNetUserAuId(auId) ?: 404 - "User not linked"
        val user = userRepo.findByAuId(auId) ?: 404 - "User not found"
        return user
    }

    data class UnlinkByRemoteReq(val auId: Long)
    @API("/unlink-by-remote")
    fun handleUnlinkByRemote(@RH(KEY_HEADER) key: Str, @RB req: UnlinkByRemoteReq): Any {
        key.checkKey()
        val user = ensureUser(req.auId)
        userFedyRepo.deleteByAquaNetUserAuId(user.auId)
        // No need to notify remote, because initiated by remote
        return SUCCESS
    }

    data class PullReq(val auId: Long, val game: Str, val exportOptions: ExportOptions)
    @API("/pull")
    fun handlePull(@RH(KEY_HEADER) key: Str, @RB req: PullReq): Any {
        key.checkKey()
        val user = ensureUser(req.auId)
        fun catched(block: () -> Any) =
            try { mapOf("result" to block()) }
            catch (e: ApiException) { mapOf("error" to mapOf("code" to e.code, "message" to e.message.toString())) }
        return when (req.game) {
            "mai2" -> catched { mai2Import.export(user, req.exportOptions) }
            else -> 406 - "Unsupported game"
        }
    }

    data class PushReq(val auId: Long, val game: Str, val data: JDict, val removeOldData: Bool)
    @Suppress("UNCHECKED_CAST")
    @API("/push")
    fun handlePush(@RH(KEY_HEADER) key: Str, @RB req: PushReq): Any {
        key.checkKey()
        val user = ensureUser(req.auId)
        val extId = user.ghostCard.extId
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
                val playlogs = req.data["userPlaylogList"] as List<JDict>
                playlogs.forEach { mai2UploadUserPlaylog.handle(mapOf("userId" to extId, "userPlaylog" to it)) }
                val userAll = req.data["upsertUserAll"] as JDict
                mai2UpsertUserAll.handle(mapOf("userId" to extId, "upsertUserAll" to userAll))
            }
            else -> 406 - "Unsupported game"
        } }

        return SUCCESS
    }

    fun onUpserted(game: Str, maybeExtId: Any?) = notifyRemote(FedyEvent.Upserted, game, maybeExtId)
    fun onImported(game: Str, maybeExtId: Any?) = notifyRemote(FedyEvent.Imported, game, maybeExtId)

    private fun notifyRemote(event: FedyEvent, game: Str, maybeExtId: Any?) { try {
        val extId = maybeExtId?.long ?: return
        val user = userRepo.findByGhostCardExtId(extId) ?: return
        val userFedy = userFedyRepo.findByAquaNetUserAuId(user.auId) ?: return
        notifyRemote(event, mapOf("auId" to user.auId, "game" to game))
    } catch (e: Exception) {
        log.error("Error handling Fedy on notifyRemote($event, $game, $maybeExtId)", e)
    } }

    private fun notifyRemote(event: FedyEvent, body: Any?) {
        val MAX_RETRY = 3
        val body = body?.toJson() ?: "{}"
        var retry = 0
        var shouldRetry = true
        while (retry < MAX_RETRY) {
            try {
                val response = "${props.remote.trimEnd('/')}/notify/${event.name}".request()
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
            "SDEZ" -> "mai2"
            else -> null // Not supported
        }
    }
}
