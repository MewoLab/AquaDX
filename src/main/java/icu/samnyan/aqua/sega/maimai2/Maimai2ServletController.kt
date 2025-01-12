package icu.samnyan.aqua.sega.maimai2

import ext.*
import icu.samnyan.aqua.net.games.mai2.Maimai2
import icu.samnyan.aqua.net.utils.ApiException
import icu.samnyan.aqua.net.utils.simpleDescribe
import icu.samnyan.aqua.sega.general.*
import icu.samnyan.aqua.sega.maimai2.handler.*
import icu.samnyan.aqua.sega.maimai2.model.Mai2Repos
import icu.samnyan.aqua.spring.Metrics
import io.ktor.client.request.*
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import java.time.format.DateTimeFormatter
import kotlin.reflect.full.declaredMemberProperties

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Suppress("unused")
@RestController
@RequestMapping(path = ["/g/mai2/Maimai2Servlet/", "/g/mai2/"])
class Maimai2ServletController(
    val upsertUserAll: UpsertUserAllHandler,
    val getUserItem: GetUserItemHandler,
    val getUserRating: GetUserRatingHandler,
    val uploadUserPhoto: UploadUserPhotoHandler,
    val uploadUserPlaylog: UploadUserPlaylogHandler,
    val getUserPortrait: GetUserPortraitHandler,
    val uploadUserPortrait: UploadUserPortraitHandler,
    val upsertUserPrint: UpsertUserPrintHandler,
    val getUserFavoriteItem: GetUserFavoriteItemHandler,
    val getUserCharacter: GetUserCharacterHandler,
    val getGameRanking: GetGameRankingHandler,
    val db: Mai2Repos,
    val net: Maimai2,
): MeowApi(serialize = { _, resp -> if (resp is String) resp else resp.toJson() }) {

    companion object {
        private val logger = LoggerFactory.getLogger(Maimai2ServletController::class.java)
        private val empty = listOf<Any>()
        private val GAME_SETTING_DATE_FMT = DateTimeFormatter.ofPattern("2010-01-01 HH:mm:00.0")
        private val GAME_SETTING_TIME_FMT = DateTimeFormatter.ofPattern("HH:mm:00")
    }

    init { initApis() }

    val endpointList = setOf("GetGameRankingApi","GetUserCharacterApi","GetUserItemApi","GetUserPortraitApi",
        "GetUserRatingApi","UploadUserPhotoApi","UploadUserPlaylogApi","UploadUserPortraitApi","UpsertUserAllApi",
        "CMGetUserCardApi","CMGetUserCardPrintErrorApi","CMGetUserDataApi","CMGetUserItemApi","CMUpsertUserPrintApi",
        "GetUserFavoriteItemApi")

    val noopEndpoint = setOf("GetUserScoreRankingApi", "UpsertClientBookkeepingApi",
        "UpsertClientSettingApi", "UpsertClientTestmodeApi", "UpsertClientUploadApi", "Ping", "RemoveTokenApi",
        "CMLoginApi", "CMLogoutApi", "CMUpsertBuyCardApi", "UserLogoutApi", "GetGameMapAreaConditionApi",
        "UpsertUserChargelogApi")

    val members = this::class.declaredMemberProperties
    val handlers: Map<String, SpecialHandler> = initH + endpointList.associateWith { api ->
        val name = api.replace("Api", "").lowercase()
        (members.find { it.name.lowercase() == name } ?: members.find { it.name.lowercase() == name.replace("cm", "") })
            ?.let { (it.call(this) as BaseHandler).toSpecial() }
            ?: initH[api] ?: initH[api.replace("CM", "")]
            ?: throw IllegalArgumentException("Mai2: No handler found for $api")
    }

    @API("/{api}")
    fun handle(@PathVariable api: String, @RequestBody data: Map<String, Any>, req: HttpServletRequest): Any {
        logger.info("Mai2 < $api : ${data.toJson()}") // TODO: Optimize logging
        if (api !in noopEndpoint && !handlers.containsKey(api)) {
            logger.warn("Mai2 > $api not found")
            return """{"returnCode":1,"apiName":"com.sega.maimai2servlet.api.$api"}"""
        }

        // Only record the counter metrics if the API is known.
        Metrics.counter("aquadx_maimai2_api_call", "api" to api).increment()

        if (api in noopEndpoint) {
            logger.info("Mai2 > $api no-op")
            return """{"returnCode":1,"apiName":"com.sega.maimai2servlet.api.$api"}"""
        }

        return try {
            Metrics.timer("aquadx_maimai2_api_latency", "api" to api).recordCallable {
                val ctx = RequestContext(req, data.mut)
                serialize(api, handlers[api]!!(ctx)).also {
                    logger.info("Mai2 > $api : ${it.truncate(1000)}")
                }
            }
        } catch (e: Exception) {
            Metrics.counter(
                "aquadx_maimai2_api_error",
                "api" to api, "error" to e.simpleDescribe()
            ).increment()

            if (e is ApiException) {
                // It's a bad practice to return 200 ok on error, but this is what maimai does so we have to follow
                return mapOf("returnCode" to 0, "apiName" to "com.sega.maimai2servlet.api.$api", "message" to "${e.message} - ${e.code}").toJson()
            } else throw e
        }
    }
}
