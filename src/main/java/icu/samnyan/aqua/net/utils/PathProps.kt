package icu.samnyan.aqua.net.utils

import ext.ensureEndingSlash
import ext.path
import jakarta.annotation.PostConstruct
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.resource.PathResourceResolver

@Configuration
@ConfigurationProperties(prefix = "paths")
class PathProps {
    var mai2Plays: String = "data/upload/mai2/plays"
    var mai2Portrait: String = "data/upload/mai2/portrait"
    var aquaNetPortrait: String = "data/upload/net/portrait"
    var recruitLog: String = "data/futari/recruit.log"

    @PostConstruct
    fun init() {
        mai2Plays = mai2Plays.path().apply { toFile().mkdirs() }.toString()
        mai2Portrait = mai2Portrait.path().apply { toFile().mkdirs() }.toString()
        aquaNetPortrait = aquaNetPortrait.path().apply { toFile().mkdirs() }.toString()
        recruitLog = recruitLog.path().apply { toFile().parentFile.mkdirs() }.toString()
    }
}

@Configuration
class UploadStatic(val paths: PathProps): WebMvcConfigurer {
    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        println("Adding resource handlers")
        mapOf(
            "/uploads/net/portrait/**" to paths.aquaNetPortrait.ensureEndingSlash(),
            "/uploads/mai2/portrait/**" to paths.mai2Portrait.ensureEndingSlash(),
            "/uploads/mai2/plays/**" to paths.mai2Plays.ensureEndingSlash()
        ).forEach { (k, v) ->
            registry.addResourceHandler(k).addResourceLocations("file:$v")
                .setCachePeriod(10).resourceChain(true).addResolver(PathResourceResolver())
        }
    }
}