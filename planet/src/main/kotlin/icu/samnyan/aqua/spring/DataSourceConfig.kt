package icu.samnyan.aqua.spring

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import java.lang.System.err
import kotlin.system.exitProcess

@Configuration
class DataSourceConfig(@Value("\${spring.datasource.url}") val databaseUrl: String) {
    init {
        if (databaseUrl.lowercase().contains("jdbc:sqlite:", ignoreCase = true)) {
            err.println("!!! ERROR !!!\n" +
                "SQLite isn't supported in the v1 development branch yet.\n" +
                "Please either switch to MariaDB or wait for the stable v1 release.\n")
            exitProcess(1)
        }
    }
}