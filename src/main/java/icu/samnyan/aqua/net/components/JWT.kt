package icu.samnyan.aqua.net.components

import ext.Str
import ext.minus
import icu.samnyan.aqua.net.db.AquaNetUser
import icu.samnyan.aqua.net.db.AquaNetUserRepo
import icu.samnyan.aqua.net.db.SessionToken
import icu.samnyan.aqua.net.db.SessionTokenRepo
import io.jsonwebtoken.JwtParser
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import jakarta.annotation.PostConstruct
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*
import javax.crypto.SecretKey

@Configuration
@ConfigurationProperties(prefix = "aqua-net.jwt")
class JWTProperties {
    var secret: Str = "Open Sesame!"
}

@Service
class JWT(
    val props: JWTProperties,
    val userRepo: AquaNetUserRepo,
    val sessionRepo: SessionTokenRepo
) {
    val log = LoggerFactory.getLogger(JWT::class.java)!!
    lateinit var key: SecretKey
    lateinit var parser: JwtParser

    @PostConstruct
    fun onLoad() {
        // Check secret
        if (props.secret == "Open Sesame!") {
            log.warn("USING DEFAULT JWT SECRET, PLEASE SET aqua-net.jwt IN CONFIGURATION")
        }

        // Pad byte array to 256 bits
        var ba = props.secret.toByteArray()
        if (ba.size < 32) {
            log.warn("JWT Secret is less than 256 bits, padding with 0. PLEASE USE A STRONGER SECRET!")
            ba = ByteArray(32).also { ba.copyInto(it) }
        }

        // Initialize key
        key = Keys.hmacShaKeyFor(ba)

        // Create parser
        parser = Jwts.parser()
            .verifyWith(key)
            .build()

        log.info("JWT Service Enabled")
    }

    @Transactional
    fun gen(user: AquaNetUser): Str {
        val activeTokens = sessionRepo.findByAquaNetUserAuId(user.auId)
            .sortedByDescending { it.expiry }.drop(4) // the cap is 5, but we append a new token after the fact
        if (activeTokens.isNotEmpty()) {
            sessionRepo.deleteAll(activeTokens)
        }
        val token = SessionToken().apply {
            aquaNetUser = user
        }
        sessionRepo.save(token)

        return Jwts.builder().header()
            .keyId("aqua-net")
            .and()
            .subject(token.token)
            .issuedAt(Date())
            .signWith(key)
            .compact()
    }

    @Transactional
    fun parse(token: Str): AquaNetUser? {
        try {
            val uuid = parser.parseSignedClaims(token).payload.subject.toString()
            val token = sessionRepo.findByToken(uuid)

            if (token != null) {
                val toBeRemoved = sessionRepo.findByAquaNetUserAuId(token.aquaNetUser.auId)
                    .filter { it.expiry < Instant.now() }
                if (toBeRemoved.isNotEmpty())
                    sessionRepo.deleteAll(toBeRemoved)
                if (token.expiry < Instant.now()) {
                    sessionRepo.delete(token)
                    return null
                }
            }

            return token?.aquaNetUser
        }  catch (e: Exception) {
            log.debug("Failed to parse JWT", e)
            return null
        }
    }

    fun auth(token: Str) = parse(token) ?: (400 - "Invalid token")

    final inline fun <T> auth(token: Str, block: (AquaNetUser) -> T) = block(auth(token))
}