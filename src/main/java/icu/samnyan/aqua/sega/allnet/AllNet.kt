package icu.samnyan.aqua.sega.allnet

import ext.*
import icu.samnyan.aqua.net.db.AquaNetUserRepo
import icu.samnyan.aqua.sega.util.AllNetBillingDecoder.decodeAllNet
import icu.samnyan.aqua.sega.util.AquaConst
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import java.io.InputStream
import java.nio.charset.StandardCharsets
import java.time.Instant
import java.time.LocalDateTime
import java.util.*

@Configuration
@ConfigurationProperties(prefix = "allnet.server")
class AllNetProps {
    var host: String = ""
    var port: Int? = null
    var hidePort: Boolean = true
    val keychipSesExpire: Long = 172800000 // milliseconds
    var checkKeychip: Boolean = false
    var keychipPermissiveForTesting: Boolean = false
    var redirect: String = "web"

    var placeName: String = ""
    var placeId: String = "123"
    var region0: String = "1"
    var regionName0: String = "W"
    var regionName1: String = "X"
    var regionName2: String = "Y"
    var regionName3: String = "Z"
    var regionCountry: String = "JPN"

    // Java assumes every application.properties as ISO-8859-1 (wtf), so we need to "correctly" convert it to UTF-8
    // More better way to this is to use XML or yaml format as these treated as UTF-8
    // but I rather use hack than breaking backward compatibility.. for now
    // TODO: Fix this
    val normalizedPlaceName: String by lazy {
        String(placeName.toByteArray(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8)
    }

    val map: Map<String, String> by lazy { mapOf(
        "stat" to "1",
        "name" to "",
        "place_id" to placeId,
        "region0" to region0,
        "region_name0" to regionName0,
        "region_name1" to regionName1,
        "region_name2" to regionName2,
        "region_name3" to regionName3,
        "country" to regionCountry,
        "nickname" to normalizedPlaceName,
    ) }
}

@Suppress("HttpUrlsUsage")
@RestController
class AllNet(
    val userRepo: AquaNetUserRepo,
    val keychipSessionService: KeychipSessionService,
    val keychipRepo: KeyChipRepo,
    val props: AllNetProps
) {
    @API("/")
    fun root(response: HttpServletResponse) {
        response.sendRedirect(props.redirect)
    }

    @API("/sys/test")
    fun selfTest() = "Server running"

    @API("/naomitest.html")
    fun naomiTest() = "naomi ok"

    @PostMapping("/sys/servlet/DownloadOrder", produces = ["text/plain"])
    fun downloadOrder(dataStream: InputStream, req: HttpServletRequest?): String {
        val bytes = dataStream.readAllBytes()
        val reqMap = decodeAllNet(bytes)

        logger.info("AllNet /DownloadOrder : $reqMap")

        val serial = reqMap["serial"] ?: AquaConst.DEFAULT_KEYCHIP_ID
        val resp = mapOf(
            "stat" to "1",
            "serial" to serial
        )

        logger.info("> Response: $resp")
        return resp.toUrl() + "\n"
    }

    @PostMapping("/sys/servlet/PowerOn", produces = ["text/plain"])
    fun powerOn(dataStream: InputStream, req: HttpServletRequest): String {
        val localAddr = req.localAddr
        val localPort = req.localPort.toString()

        // game_id SDEZ, ver 1.35, serial A0000001234, ip, firm_ver 50000, boot_ver 0000,
        // encode UTF-8, format_ver 3, hops 1， token 2010451813
        val reqMap = decodeAllNet(dataStream.readAllBytes())
        val serial = reqMap["serial"] ?: ""
        logger.info("AllNet /PowerOn : $reqMap")

        var session: String? = null

        // Proper keychip authentication
        if (props.checkKeychip) {
            // If it's a user keychip, it should be in user database
            val u = userRepo.findByKeychip(serial)
            if (u != null) {
                // Create a new session for the user
                logger.info("> Keychip authenticated: ${u.auId} ${u.computedName}")
                session = keychipSessionService.new(u, reqMap["game_id"] ?: "").token
            }

            // Check if it's a whitelisted keychip
            else if (!serial.isEmpty() && keychipRepo.existsByKeychipId(serial)) {
                session = keychipSessionService.new(null, reqMap["game_id"] ?: "").token
            }

            else if (props.keychipPermissiveForTesting) {
                logger.warn("> Accepted invalid keychip $serial in permissive mode")
                session = keychipSessionService.new(null, reqMap["game_id"] ?: "").token
            }

            else {
                // This will cause an allnet auth bad on client side
                return "".also { logger.warn("> Rejected: Keychip not found") }
            }
        }

        val gameId = reqMap["game_id"] ?: return "".also { logger.warn("> Rejected: No game_id provided") }
        val ver = reqMap["ver"] ?: "1.0"

        val formatVer = reqMap["format_ver"] ?: ""
        val resp = props.map.mut + mapOf(
            "uri" to switchUri(localAddr, localPort, gameId, ver, session),
            "host" to props.host.ifBlank { localAddr },
        )

        // Different responses for different versions
        if (formatVer.startsWith("2")) {
            val now = LocalDateTime.now()
            resp += mapOf(
                "year" to now.year,
                "month" to now.month.value,
                "day" to now.dayOfMonth,
                "hour" to now.hour,
                "minute" to now.minute,
                "second" to now.second,
                "setting" to "1",
                "timezone" to "+09:00",
                "res_class" to "PowerOnResponseV2"
            ).mapValues { it.value.toString() }
        } else {
            resp += mapOf(
                "allnet_id" to "456",
                "client_timezone" to "+0900",
                "utc_time" to Instant.now().toString().substring(0, 19) + "Z",
                "setting" to "",
                "res_ver" to "3",
                "token" to reqMap["token"]
            ).mapValues { it.value.toString() }
        }

        logger.info("> Response: $resp")
        return resp.toUrl() + "\n"
    }

    private fun switchUri(localAddr: Str, localPort: Str, gameId: Str, ver: Str, session: Str?): Str {
        val addr = props.host.ifBlank { localAddr } +
            if (props.hidePort) "" else ":${props.port ?: localPort}"

        // If keychip authentication is enabled, the game URLs will be set to /gs/{token}/{game}/...
        val base = if (session != null) "gs/$session" else "g"

        return "http://$addr/$base/" + when (gameId) {
            "SDBT" -> "chu2/$ver/$session/"
            "SDHD" -> "chu3/$ver/"
            "SDGS" -> "chu3/$ver/" // International (c3exp)
            "SBZV" -> "diva/"
            "SDDT" -> "ongeki/"
            "SDEY" -> "mai/"
            "SDGA" -> "mai2/" // International (Exp)
            "SDGB" -> "mai2/" // International (China) - TODO: Test it
            "SDEZ" -> "mai2/"
            "SDFE" -> "wacca" // Note: Wacca must not end with a trailing slash
            "SDED" -> "card/"
            else -> ""
        }
    }

    companion object {
        val logger = logger()
    }
}

