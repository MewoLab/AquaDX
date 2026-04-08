package icu.samnyan.aqua.sega.ongeki

import ext.*
import icu.samnyan.aqua.net.db.AquaUserServices
import icu.samnyan.aqua.net.utils.simpleDescribe
import icu.samnyan.aqua.sega.allnet.TokenChecker
import icu.samnyan.aqua.sega.general.GameMusicPopularity
import icu.samnyan.aqua.sega.general.MeowApi
import icu.samnyan.aqua.sega.general.RequestContext
import icu.samnyan.aqua.sega.general.service.CardService
import icu.samnyan.aqua.sega.maimai2.Maimai2ServletController
import icu.samnyan.aqua.sega.util.BasicMapper
import icu.samnyan.aqua.spring.Metrics
import jakarta.servlet.http.HttpServletRequest
import org.springframework.web.bind.annotation.RestController
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec


@Suppress("unused")
@RestController
@API("/g/SDDT/{version}", "/g/SDDT")
class OngekiController(
    val mapper: BasicMapper,
    val db: OngekiUserRepos,
    val gdb: OngekiGameRepos,
    val us: AquaUserServices,
    val pop: GameMusicPopularity,
    val cardService: CardService,
): MeowApi(
    { _, resp -> if (resp is String) resp else mapper.write(resp) },
    @OptIn(ExperimentalStdlibApi::class)
    { endpoint: String ->
        gdb.gameData.ogkGameEncryption.map{
            SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
                .generateSecret(PBEKeySpec(
                    endpoint.toCharArray(),
                    it.salt!!.hexToByteArray(),
                    it.iterations, 128
                )).encoded.toHexString().substring(0, 32)
        }
    }
) {

    val log = logger()

    init { ongekiInit() }
    val handlers = initH

    @API("/{api}")
    fun handle(@PV api: Str, @RB data: MutableMap<Str, Any>, @PV version: Str?, req: HttpServletRequest): Any {
        val ctx = RequestContext(req, data)
        version?.let { data["version"] = it }

        val token = TokenChecker.tokenShort()

        val apiFriendlyName = if (api.lowercase() == api && api.length == 32)
            ((hashes.filter { it.value.contains(api) }.keys.firstOrNull()?.str + " (encrypt)") ?: api) else "$api (encrypt)"
        log.info("$token : $apiFriendlyName < ${data.toJson()}")

        val noop = """{"returnCode":1,"apiName":"${api.substringBefore("Api").firstCharLower()}"}"""
        if (!handlers.containsKey(api)) {
            log.warn("$token : $apiFriendlyName > not found")
            return noop
        }

        // Only record the counter metrics if the API is known.
        Metrics.counter("aquadx_ongeki_api_call", "api" to api).increment()

        return try {
            Metrics.timer("aquadx_ongeki_api_latency", "api" to api).recordCallable {
                serialize(api, handlers[api]!!(ctx) ?: noop).also {
                    if (api !in setOf("GetUserItemApi", "GetGameEventApi"))
                        log.info("$token : $apiFriendlyName > ${it.truncate(500)}")
                }
            }
        } catch (e: Exception) {
            Metrics.counter("aquadx_ongeki_api_error", "api" to api, "error" to e.simpleDescribe()).increment()
            throw e
        }
    }
}
