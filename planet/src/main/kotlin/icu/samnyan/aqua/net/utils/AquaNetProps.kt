package icu.samnyan.aqua.net.utils

import jakarta.annotation.PostConstruct
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import kotlin.io.path.Path
import kotlin.io.path.createDirectories

@Configuration
@ConfigurationProperties(prefix = "aqua-net")
class AquaNetProps {
    var linkCardLimit: Int = 10
    var importBackupPath = "data/import-backups"

    @PostConstruct
    fun init() {
        Path(importBackupPath).createDirectories()
    }
}