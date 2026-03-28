package icu.samnyan.aqua.sega.allnet

import ext.Str
import icu.samnyan.aqua.net.FrontierProps
import icu.samnyan.aqua.net.components.GeoIP
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletRequestWrapper
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


/**
 * Handle token check for secure requests.
 *
 * When checkKeychip is enabled, the game URLs will be set to /gs/SS{token}/{game}/...
 * This interceptor will check if the token exists in the database.
 */
@Component
@ConditionalOnBean(AllNetSecureInit::class)
class TokenChecker(
    val keyChipRepo: KeyChipRepo,
    val keychipSessionService: KeychipSessionService,
    val frontierProps: FrontierProps,
    val geoip: GeoIP
) : HandlerInterceptor {
    val log = LoggerFactory.getLogger(TokenChecker::class.java)

    companion object {
        private val log = LoggerFactory.getLogger(TokenChecker::class.java)

        private val currentSession = ThreadLocal<KeychipSession?>()
        fun getCurrentSession() = currentSession.get()
        fun tokenShort() = getCurrentSession()?.token?.substring(0, 6) ?: "NO-TOKEN"
    }

    /**
     * Handle request before it's processed.
     */
    override fun preHandle(req: HttpServletRequest, resp: HttpServletResponse, handler: Any): Boolean {
        // Skip the interceptor if the request is already forwarded
        if (req.getAttribute("token") != null) return true
        val ip = geoip.getIP(req)

        // Parse the token from the request path
        val token = extractTokenFromPath(req.requestURI)
        log.debug("PreHandle: ${req.requestURI} from ip $ip, token: $token")

        // Check whether the token exists in the database
        // The token can either be a keychip id (old method) or a session id (new method)
        // Or the frontier token
        val session = keychipSessionService.find(token)
        if (token.isNotBlank() && (keyChipRepo.existsByKeychipId(token) || session != null
                || (frontierProps.enabled && frontierProps.ftk == token)))
        {
            currentSession.set(session)

            // Forward the request
            val w = RewriteWrapper(req, token).apply { setAttribute("token", token) }
            req.getRequestDispatcher(w.requestURI).forward(w, resp)

            // Prevent the request from being processed twice
            return false
        }

        // Token doesn't exist, reject the request
        resp.status = HttpServletResponse.SC_FORBIDDEN
        log.warn("Request rejected: ${req.requestURI} from ip $ip")
        return false
    }

    /**
     * Extract the token from the request path.
     * Example: "/gs/12033897/mai2/SomeEndpoint" -> "12033897"
     */
    fun extractTokenFromPath(path: String) = path.substringAfter("/gs/", "").substringBefore("/", "")
}

/**
 * Request wrapper for rewriting the URI after token check.
 */
class RewriteWrapper(val req: HttpServletRequest, token: Str) : HttpServletRequestWrapper(req) {
    val replace = "/gs/$token/"
    val newUri = req.requestURI.replace(replace, "/g/")
    val newUrl = req.requestURL.toString().replace(replace, "/g/")
    val newSp = req.servletPath.replace(replace, "/g/")

    override fun getHeader(name: String): String? {
        if (name == "wrapper original url") return req.requestURL.toString()
        return super.getHeader(name)
    }

    override fun getRequestURI() = newUri
    override fun getRequestURL() = StringBuffer(newUrl)
    override fun getServletPath() = newSp
}

/**
 * Register the token checker interceptor
 */
@Configuration
@ConditionalOnProperty(
    value = ["allnet.server.check-keychip"],
    havingValue = "true",
    matchIfMissing = false
)
class AllNetSecureInit(
    val tokenChecker: TokenChecker,
) : WebMvcConfigurer {
    val log = LoggerFactory.getLogger(AllNetSecureInit::class.java)

    override fun addInterceptors(reg: InterceptorRegistry) {
        log.info("AllNet: Added token interceptor to secure requests.")
        reg.addInterceptor(tokenChecker).addPathPatterns("/gs/**", "/g/**")
    }
}
