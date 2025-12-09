package icu.samnyan.aqua.sega.ongeki

import ext.*
import icu.samnyan.aqua.net.db.AquaUserServices
import icu.samnyan.aqua.net.utils.simpleDescribe
import icu.samnyan.aqua.sega.allnet.TokenChecker
import icu.samnyan.aqua.sega.general.GameMusicPopularity
import icu.samnyan.aqua.sega.general.MeowApi
import icu.samnyan.aqua.sega.general.RequestContext
import icu.samnyan.aqua.sega.general.service.CardService
import icu.samnyan.aqua.sega.util.jackson.BasicMapper
import icu.samnyan.aqua.spring.Metrics
import jakarta.servlet.http.HttpServletRequest
import org.springframework.web.bind.annotation.RestController
import kotlin.collections.set

@Suppress("unused")
@RestController
@API("/g/ongeki/{version}", "/g/ongeki")
class OngekiController(
    val mapper: BasicMapper,
    val db: OngekiUserRepos,
    val gdb: OngekiGameRepos,
    val us: AquaUserServices,
    val pop: GameMusicPopularity,
    val cardService: CardService,
): MeowApi({ _, resp -> if (resp is String) resp else mapper.write(resp) }) {

    val log = logger()

    val noopEndpoint = setOf("ExtendLockTimeApi", "GameLogoutApi", "RegisterPromotionCardApi",
        "UpsertClientBookkeepingApi", "UpsertClientDevelopApi", "UpsertClientErrorApi", "UpsertClientSettingApi",
        "UpsertClientTestmodeApi", "UpsertUserGplogApi", "Ping")

    init { ongekiInit() }
    val handlers = initH

    @API("/{api}")
    fun handle(@PV api: Str, @RB data: MutableMap<Str, Any>, @PV version: Str?, req: HttpServletRequest): Any {
        val ctx = RequestContext(req, data)
        version?.let { data["version"] = it }

        val token = TokenChecker.tokenShort()
        log.info("$token : $api < ${data.toJson()}")

        val noop = """{"returnCode":1,"apiName":"${api.substringBefore("Api").firstCharLower()}"}"""
        if (api !in noopEndpoint && !handlers.containsKey(api)) {
            log.warn("$token : $api > not found")
            return noop
        }

        // Only record the counter metrics if the API is known.
        Metrics.counter("aquadx_ongeki_api_call", "api" to api).increment()
        if (api in noopEndpoint) {
            log.info("$token : $api > no-op")
            return noop
        }

        return try {
            Metrics.timer("aquadx_ongeki_api_latency", "api" to api).recordCallable {
                serialize(api, handlers[api]!!(ctx) ?: noop).also {
                    if (api !in setOf("GetUserItemApi", "GetGameEventApi"))
                        log.info("$token : $api > ${it.truncate(500)}")
                }
            }
        } catch (e: Exception) {
            Metrics.counter("aquadx_ongeki_api_error", "api" to api, "error" to e.simpleDescribe()).increment()
            throw e
        }
    }
}
