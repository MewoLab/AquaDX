package icu.samnyan.aqua.sega.general.filter

import ext.details
import ext.logger
import ext.toJson
import icu.samnyan.aqua.net.components.GeoIP
import icu.samnyan.aqua.sega.allnet.TokenChecker
import icu.samnyan.aqua.sega.general.model.GameEncryptionKey
import icu.samnyan.aqua.sega.util.GameDataService
import icu.samnyan.aqua.sega.util.ZLib
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.eclipse.jetty.io.EofException
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.ContentCachingResponseWrapper
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
class CompressionFilter(
    val geoip: GeoIP,
    val gameData: GameDataService
) : OncePerRequestFilter() {
    companion object {
        val log = logger()
        val b64d = Base64.getMimeDecoder()
        val b64e = Base64.getMimeEncoder()

    }

    var keys = gameData.chu3GameEncryption + gameData.mai2GameEncryption + gameData.ogkGameEncryption
    fun getEncryptionKeys(path: String): GameEncryptionKey? {
        val endpoint = path.split("/").last()
        if (endpoint.lowercase() != endpoint || endpoint.length != 32) return null

        val game = path.split("/")[2]
        val version = path.split("/")[3].filterNot { it == '.' }.toInt()
        return keys.find { it.versions.contains(version) && it.code == game }
    }

    @OptIn(ExperimentalStdlibApi::class)
    override fun doFilterInternal(req: HttpServletRequest, resp: HttpServletResponse, chain: FilterChain) {
        val isDeflate = req.getHeader("content-encoding") == "deflate"
        val isDfi = req.getHeader("pragma") == "DFI"

        val keys = getEncryptionKeys(req.servletPath)

        // Decode input
        val reqSrc = try {
            req.inputStream.readAllBytes().let {
                if (keys != null) {
                    val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
                    cipher.init(
                        Cipher.DECRYPT_MODE,
                        SecretKeySpec(keys.key!!.hexToByteArray(), "AES"),
                        IvParameterSpec(keys.iv!!.hexToByteArray())
                    )
                    cipher.doFinal(it)
                } else it
            }.let {
                if (isDeflate) ZLib.decompress(it)
                else if (isDfi) ZLib.decompress(b64d.decode(it))
                else it
            }
        } catch (e: Exception) {
            log.error("Failed to decode request from ip ${geoip.getIP(req)}")
            resp.sendError(400, "Failed to decode request")
            return
        }

        // Handle request
        val respW = ContentCachingResponseWrapper(resp)
        val result = try {
            chain.doFilter(CompressRequestWrapper(req, reqSrc), respW)
            ZLib.compress(respW.contentAsByteArray)
                .let { if (isDfi) b64e.encode(it) else it }
                .let {
                    if (keys != null) {
                        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
                        cipher.init(
                            Cipher.ENCRYPT_MODE,
                            SecretKeySpec(keys.key!!.hexToByteArray(), "AES"),
                            IvParameterSpec(keys.iv!!.hexToByteArray())
                        )
                        cipher.doFinal(it)
                    } else it
                }
        } finally {
            if (respW.status != 200) {
                val details = mapOf(
                    "req" to req.details(),
                    "resp" to respW.details(),
                    "body" to reqSrc.toString(Charsets.UTF_8),
                    "result" to respW.contentAsByteArray.toString(Charsets.UTF_8),
                    "token" to TokenChecker.getCurrentSession()?.token
                ).toJson()

                log.error("HTTP ${respW.status}: $details")
            }
        }

        // Write response
        resp.setContentLength(result.size)
        if (isDfi) resp.setHeader("pragma", "DFI")
        else {
            resp.contentType = "application/json; charset=utf-8"
            resp.setHeader("content-encoding", "deflate")
        }

        try {
            resp.outputStream.use { it.write(result); it.flush() }
        } catch (e: EofException) {
            log.warn("- EOF: Client closed connection when writing result")
        }
    }

    /** Only games (other than WACCA) require response compression */
    override fun shouldNotFilter(req: HttpServletRequest) =
        !(req.servletPath.startsWith("/g/") && !req.servletPath.startsWith("/g/wacca"))
}
