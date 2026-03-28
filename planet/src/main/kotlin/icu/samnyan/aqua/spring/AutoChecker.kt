package icu.samnyan.aqua.spring

import icu.samnyan.aqua.sega.aimedb.AimeDbProps
import icu.samnyan.aqua.sega.allnet.AllNetProps
import org.apache.hc.client5.http.impl.classic.HttpClients
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactoryBuilder
import org.apache.hc.client5.http.ssl.TrustAllStrategy
import org.apache.hc.core5.ssl.SSLContextBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ResourceLoader
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import java.lang.System.err
import java.net.Socket

/**
 * A simple boot check to warn user if there is some wrong config
 *
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
class AutoChecker(
    val allNetProps: AllNetProps,
    val aimedb: AimeDbProps,
    @param:Value("\${server.port:}") private val SERVER_PORT: String,
    @param:Value("\${billing.server.port}") private val BILLING_PORT: Int,
    @param:Value("\${billing.server.enable}") private val BILLING_ENABLED: Boolean,
    @param:Value("\${aquaviewer.server.enable}") private val AQUAVIEWER_ENABLED: Boolean,
    @param:Value("\${build.version:N/A}") private val VERSION_TAG: String,
    @param:Value("\${build.timestamp:N/A}") private val BUILD_TIMESTAMP: String,
    resLoader: ResourceLoader,
) {
    val splash = resLoader.getResource("classpath:splash/splash.txt").inputStream.bufferedReader().readText()
    val allPerfect = resLoader.getResource("classpath:splash/all-perfect.txt").inputStream.bufferedReader().readText()
    var failDetail = ""
    val host = allNetProps.host.ifEmpty { "127.0.0.1" }
    val port = allNetProps.port ?: SERVER_PORT.toInt()

    companion object {
        const val SUCCESS = "✅"
        const val ERROR = "❌"
    }

    /**
     * Aime DB: try open socket to Aime DB port (default 22345)
     * TODO: Sending hello request would be more reliable than testing if port is open
     */
    fun checkAimeDB() {
        print("Aime DB : Port ${aimedb.port} ")
        if (!aimedb.enable) return println("SKIP (DISABLED)")

        val address = aimedb.address.replace("0.0.0.0", "127.0.0.1")
        try {
            Socket(address, aimedb.port).use { println(SUCCESS) }
        } catch (e: Exception) {
            println(ERROR)
            failDetail += "Aime DB self-test raised an exception during testing\n"
            failDetail += "Exception: $e\n"
        }
    }

    fun checkBilling() {
        print("Billing : Port $BILLING_PORT ")
        if (!BILLING_ENABLED) return println("SKIP (DISABLED)")

        try {
            // Do not validate SSL certificate (self-signed ib cert)
            val rt = HttpComponentsClientHttpRequestFactory().apply {
                httpClient = HttpClients.custom()
                    .setConnectionManager(PoolingHttpClientConnectionManagerBuilder.create()
                        .setSSLSocketFactory(SSLConnectionSocketFactoryBuilder.create()
                            .setHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                            .setSslContext(SSLContextBuilder.create()
                                .loadTrustMaterial(TrustAllStrategy.INSTANCE)
                                .build()).build()).build()).build()
            }.let { RestTemplate(it) }

            val url = "https://${aimedb.address}:$BILLING_PORT/sys/test"
            val resp = rt.getForEntity(url, String::class.java)
            if (resp.statusCode.is2xxSuccessful && resp.body == "Server running") {
                println(SUCCESS)
            } else {
                println(ERROR)
                failDetail += "Billing self-test failed on $url\n"
                failDetail += "${resp.statusCode.value()} : ${resp.body}\n"
                failDetail += "Check if billing port being used by other application.\n"
            }
        } catch (e: Exception) {
            println(ERROR)
            failDetail += "Billing self-test raised an exception during testing\n"
            failDetail += "Exception: $e\n"
        }
    }

    fun checkAllNet() {
        print("ALL.Net : Port $port ")
        if (allNetProps.host == "localhost" || allNetProps.host.startsWith("127.")) {
            print("⚠️ ")
            failDetail += "ALL.Net host is currently using loopback address.\n"
            failDetail += "Game might not connect to server with this. If it was not intentional, please edit configuration file.\n"
        }

        val restTemplate = RestTemplate()
        val url = "http://$host:$port/sys/test"
        try {
            val resp = restTemplate.getForEntity(url, String::class.java)
            if (resp.statusCode.is2xxSuccessful && resp.body == "Server running") {
                println(SUCCESS)
            } else {
                println(ERROR)
                failDetail += "ALL.Net self-test could not connect to $url\n"
                failDetail += "Status code: ${resp.statusCode.value()}\n"
            }
        } catch (e: Exception) {
            println(ERROR)
            failDetail += "ALL.Net self-test raised an exception during testing$url\n"
            failDetail += "Exception: $e\n"
        }
    }

    fun check() {
        println(splash)
        println("""
            AquaDX Local Arcade Server
            Version: $VERSION_TAG (Built on $BUILD_TIMESTAMP)
            
            This is a free open-source software. If you paid for it, you were scammed.
            
            [Self testing]
        """.trimIndent())

        checkAimeDB()
        checkBilling()
        checkAllNet()

        if (failDetail.isEmpty()) {
            println(allPerfect)
        } else {
            err.println("> Self-tests failed.")
            err.println(failDetail)
        }
    }
}
