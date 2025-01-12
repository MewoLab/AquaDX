package icu.samnyan.aqua.sega.billing

import ext.logger
import ext.toUrl
import icu.samnyan.aqua.sega.util.AllNetBillingDecoder.decodeBilling
import jakarta.annotation.PostConstruct
import jakarta.servlet.http.HttpServletRequest
import org.eclipse.jetty.http.HttpVersion
import org.eclipse.jetty.server.*
import org.eclipse.jetty.util.resource.URLResourceFactory
import org.eclipse.jetty.util.ssl.SslContextFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.web.embedded.jetty.JettyServerCustomizer
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory
import org.springframework.boot.web.server.WebServerFactoryCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ResourceLoader
import org.springframework.util.FileCopyUtils
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.Signature
import java.security.spec.PKCS8EncodedKeySpec

@Configuration
@ConfigurationProperties(prefix = "billing.server")
class BillingProps {
    var enable: Boolean = true
    var port: Int = 8443
}

@RestController
class Billing(
    @param:Value("\${server.port}") private val SERVER_PORT: Int,
    val resourceLoader: ResourceLoader,
    val props: BillingProps
) {
    lateinit var billingKey: PrivateKey

    val logger = logger()

    @PostConstruct
    fun init() {
        val keyRes = resourceLoader.getResource("classpath:billing.der")
        val key = FileCopyUtils.copyToByteArray(keyRes.inputStream)
        billingKey = KeyFactory.getInstance("RSA").generatePrivate(PKCS8EncodedKeySpec(key))
    }

    @PostMapping("/request", produces = ["text/plain"])
    fun powerOn(dataStream: InputStream, req: HttpServletRequest?): String {
        val bytes = dataStream.readAllBytes()
        val reqMap = decodeBilling(bytes)

        logger.info("Billing : $reqMap")

        val keychipId = reqMap["keychipid"] ?: ""
        val resp = mapOf(
            "result" to 0,
            "waitTime" to 100,
            "lineLimit" to 1,
            "message" to "",
            "playLimit" to 1024,
            "playLimitSig" to sign(keychipId, 1024),
            "protocolVer" to "1.000",
            "nearFull" to 66048,  // 0x00010200
            "nearFullSig" to sign(keychipId, 66048),
            "fixLogCnt" to 0,
            "fixInterval" to 5,
            "playHistory" to "000000/0:000000/0:000000/0"
        )
        logger.info("> Response: $resp")
        return resp.mapKeys { it.key.lowercase() }.toUrl() + "\n"
    }

    /**
     * Sign the value with billing key
     */
    @OptIn(ExperimentalStdlibApi::class)
    private fun sign(keychipId: String, value: Int) = try {
        Signature.getInstance("SHA1withRSA").run {
            initSign(billingKey)
            update(ByteBuffer.allocate(15).apply {
                order(ByteOrder.LITTLE_ENDIAN)
                putInt(0, value)
                put(4, keychipId.toByteArray())
            })
            sign().toHexString()
        }
    } catch (e: Exception) {
        logger.error("Failed to sign with billing key, " + e.message)
        ""
    }

    /**
     * Customizer for the Jetty server
     */
    fun getCustomizer() = object : JettyServerCustomizer {
        override fun customize(server: Server) {
            ServerConnector(server).use { conn ->
                conn.port = SERVER_PORT
                if (!props.enable) {
                    server.connectors = arrayOf<Connector>(conn)
                    return@use
                }

                val sslContext = SslContextFactory.Server().apply {
                    keyStoreResource = URLResourceFactory().newResource(javaClass.classLoader.getResource("server.p12"))
                    keyStorePassword = "aquaserver"
                    certAlias = "ib"
                    isSniRequired = false
                    setExcludeCipherSuites(*excludeCipherSuites.filter { it != "^TLS_RSA_.*$" }.toTypedArray())
                }

                ServerConnector(server,
                    SslConnectionFactory(sslContext, HttpVersion.HTTP_1_1.asString()),
                    HttpConnectionFactory(HttpConfiguration().apply {
                        addCustomizer(SecureRequestCustomizer().apply { isSniHostCheck = false })
                    })
                ).use { httpsConnector ->
                    httpsConnector.port = props.port
                    server.connectors = arrayOf<Connector>(conn, httpsConnector)
                }
            }
        }
    }

    @Bean
    fun webServerFactoryCustomizer() = WebServerFactoryCustomizer<JettyServletWebServerFactory> {
        it.addServerCustomizers(getCustomizer())
    }
}
