package icu.samnyan.aqua.sega.general.filter

import ext.details
import ext.logger
import ext.toJson
import icu.samnyan.aqua.sega.allnet.TokenChecker
import icu.samnyan.aqua.sega.util.ZLib
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.eclipse.jetty.io.EofException
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.ContentCachingResponseWrapper
import java.util.*


/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
class CompressionFilter : OncePerRequestFilter() {
    companion object {
        val log = logger()
        val b64d = Base64.getMimeDecoder()
        val b64e = Base64.getMimeEncoder()
    }

    override fun doFilterInternal(req: HttpServletRequest, resp: HttpServletResponse, chain: FilterChain) {
        val isDeflate = req.getHeader("content-encoding") == "deflate"
        val isDfi = req.getHeader("pragma") == "DFI"

        // Decode input
        val reqSrc = req.inputStream.readAllBytes().let {
            if (isDeflate) ZLib.decompress(it)
            else if (isDfi) ZLib.decompress(b64d.decode(it))
            else it
        }

        // Handle request
        val respW = ContentCachingResponseWrapper(resp)
        val result = try {
            chain.doFilter(CompressRequestWrapper(req, reqSrc), respW)
            ZLib.compress(respW.contentAsByteArray).let { if (isDfi) b64e.encode(it) else it }
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
