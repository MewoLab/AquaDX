package icu.samnyan.aqua.sega.cardmaker

import ext.API
import ext.logger
import ext.long
import ext.parsing
import icu.samnyan.aqua.sega.allnet.TokenChecker
import icu.samnyan.aqua.sega.util.BasicMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.InetAddress
import java.net.UnknownHostException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@RestController
@RequestMapping("/g/card")
class CardMakerController(
    val mapper: BasicMapper,
    @param:Value("\${allnet.server.host:}") val ALLNET_HOST: String,
    @param:Value("\${allnet.server.port:}") val ALLNET_PORT: String,
    @param:Value("\${server.port:}") val SERVER_PORT: String
) {
    val logger = logger()

    @API("GetGameSettingApi")
    fun getGameSetting(@ModelAttribute request: MutableMap<String, Any>): Any? {
        val formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss")
        val rebootStartTime = LocalDateTime.now().minusHours(3)
        val rebootEndTime = LocalDateTime.now().minusHours(2)

        val gameSetting = mapOf(
            "dataVersion" to "1.35.0",
            "ongekiCmVersion" to "1.32.0",
            "chuniCmVersion" to "1.30.0",
            "maimaiCmVersion" to "1.45.0",
            "isMaintenance" to false,
            "requestInterval" to 10,
            "rebootStartTime" to rebootStartTime.format(formatter),
            "rebootEndTime" to rebootEndTime.format(formatter),
            "isBackgroundDistribute" to false,
            "maxCountCharacter" to 100,
            "maxCountItem" to 100,
            "maxCountCard" to 100,
            "watermark" to false
        )

        val json = mapper.write(mapOf(
            "gameSetting" to gameSetting,
            "isDumpUpload" to false,
            "isAou" to false
        ))

        logger.info("Response: {}", json)
        return json
    }

    fun gameConnect(modelKind: Int, modelVersion: Int, url: String) =
        mapOf("modelKind" to modelKind, "modelVersion" to modelVersion, "url" to url)

    @API("GetGameConnectApi")
    fun getGameConnect(@ModelAttribute request: MutableMap<String, Any>): Any? {
        val version = parsing { request["version"]!!.long } // Rom version
        val session = TokenChecker.Companion.getCurrentSession()

        val addr = ALLNET_HOST.ifBlank { null } ?:
        try { InetAddress.getLocalHost().hostAddress }
        catch (_: UnknownHostException) { "localhost" }
        val port = ALLNET_PORT.ifBlank { null } ?: SERVER_PORT

        val base = if (session == null) "/g" else "/gs/" + session.token
        val json = mapper.write(mapOf(
            "length" to 3,
            "gameConnectList" to listOf(
                gameConnect(0, 1, "http://$addr:$port$base/chu3/$version/"),
                gameConnect(1, 1, "http://$addr:$port$base/mai2/"),
                gameConnect(2, 1, "http://$addr:$port$base/ongeki/")
            )
        ))

        logger.info("Response: $json")
        return json
    }

    @API("GetClientBookkeepingApi")
    fun getClientBookkeeping(@ModelAttribute request: MutableMap<String, Any>): Any? {
        val placeId = parsing { request["placeId"]!!.long }
        val json = mapper.write(mapOf(
            "placeId" to placeId,
            "length" to 0,
            "clientBookkeepingList" to mutableListOf<Any>()
        ))

        logger.info("Response: $json")
        return json
    }

    @API("UpsertClientBookkeepingApi")
    fun upsertClientBookkeeping() = "{\"returnCode\":1,\"apiName\":\"UpsertClientBookkeepingApi\"}"

    @API("UpsertClientSettingApi")
    fun upsertClientSetting() = "{\"returnCode\":1,\"apiName\":\"UpsertClientSettingApi\"}"
}