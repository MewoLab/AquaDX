package icu.samnyan.aqua.sega.chusan

import ext.*
import icu.samnyan.aqua.net.db.AquaUserServices
import icu.samnyan.aqua.net.utils.simpleDescribe
import icu.samnyan.aqua.sega.allnet.TokenChecker
import icu.samnyan.aqua.sega.chusan.handler.GameLoginHandler
import icu.samnyan.aqua.sega.chusan.handler.UpsertUserAllHandler
import icu.samnyan.aqua.sega.chusan.handler.chusanInit
import icu.samnyan.aqua.sega.chusan.model.Chu3Repos
import icu.samnyan.aqua.sega.general.*
import icu.samnyan.aqua.sega.util.jackson.BasicMapper
import icu.samnyan.aqua.sega.util.jackson.StringMapper
import icu.samnyan.aqua.spring.Metrics
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.RestController
import kotlin.collections.set
import kotlin.reflect.full.declaredMemberProperties

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Suppress("unused")
@RestController
@API(value = ["/g/chu3/{version}/ChuniServlet", "/g/chu3/{version}"])
class ChusanController(
    val gameLogin: GameLoginHandler,
    val upsertUserAll: UpsertUserAllHandler,

    val mapper: StringMapper,
    val cmMapper: BasicMapper,
    val db: Chu3Repos,
    val us: AquaUserServices,
    val versionHelper: ChusanVersionHelper,
    val props: ChusanProps
): MeowApi({ api, resp ->
    if (resp is String) resp
    else (if ("CM" in api) cmMapper else mapper).write(resp)
}) {
    val log = LoggerFactory.getLogger(ChusanController::class.java)

    // Below are code related to handling the handlers
    val externalHandlers = mutableListOf("GameLoginApi", "UpsertUserAllApi")

    val noopEndpoint = setOf("UpsertClientBookkeepingApi", "UpsertClientDevelopApi", "UpsertClientErrorApi",
        "UpsertClientSettingApi", "UpsertClientTestmodeApi", "CreateTokenApi", "RemoveTokenApi", "UpsertClientUploadApi",
        "PrinterLoginApi", "PrinterLogoutApi", "Ping", "GameLogoutApi", "RemoveMatchingMemberApi")

    init { chusanInit() }

    val members = this::class.declaredMemberProperties
    val handlers: Map<String, SpecialHandler> = initH + externalHandlers.associateWith { api ->
        val name = api.replace("Api", "").lowercase()
        (members.find { it.name.lowercase() == name } ?: members.find { it.name.lowercase() == name.replace("cm", "") })
            ?.let { (it.call(this) as BaseHandler).toSpecial() }
            ?: throw IllegalArgumentException("Chu3: No handler found for $api")
    }

    @API("/{endpoint}", "/MatchingServer/{endpoint}")
    fun handle(@PV endpoint: Str, @RB data: MutableMap<Str, Any>, @PV version: Str, req: HttpServletRequest): Any {
        val ctx = RequestContext(req, data)
        var api = endpoint
        data["version"] = version

        // Export version
        if (api.endsWith("C3Exp")) {
            api = api.removeSuffix("C3Exp")
            data["c3exp"] = true
        }

        if (api.startsWith("CM") && api !in handlers) api = api.removePrefix("CM")
        val token = TokenChecker.getCurrentSession()?.token?.substring(0, 6) ?: "NO-TOKEN"
        log.info("Chu3 < $api : ${data.toJson()} : [$token]")

        if (api !in noopEndpoint && !handlers.containsKey(api)) {
            log.warn("Chu3 > $api not found")
            return """{"returnCode":"1","apiName":"$api"}"""
        }

        // Only record the counter metrics if the API is known.
        Metrics.counter("aquadx_chusan_api_call", "api" to api).increment()
        if (api in noopEndpoint) {
            log.info("Chu3 > $api no-op")
            return """{"returnCode":"1"}"""
        }

        return try {
            Metrics.timer("aquadx_chusan_api_latency", "api" to api).recordCallable {
                serialize(api, handlers[api]!!(ctx)).also {
                    if (api !in setOf("GetUserItemApi", "GetGameEventApi"))
                        log.info("Chu3 > $api : $it")
                }
            }
        } catch (e: Exception) {
            Metrics.counter(
                "aquadx_chusan_api_error",
                "api" to api, "error" to e.simpleDescribe()
            ).increment()
            throw e
        }
    }
}
