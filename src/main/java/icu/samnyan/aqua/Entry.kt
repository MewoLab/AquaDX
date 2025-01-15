package icu.samnyan.aqua

import icu.samnyan.aqua.sega.aimedb.AimeDbServer
import icu.samnyan.aqua.sega.maimai2.worldslink.MaimaiFutari
import icu.samnyan.aqua.spring.AutoChecker
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.scheduling.annotation.EnableScheduling
import java.io.File

@SpringBootApplication
@EnableScheduling
class Entry

fun main(args: Array<String>) {
    if (args.getOrNull(0) == "futari") {
        // Run futari main
        return MaimaiFutari().start()
    }

    // If data/ is not found, create it
    File("data").mkdirs()

    // Run the application
    val ctx = SpringApplication.run(Entry::class.java, *args)

    // Start the AimeDbServer
    val aimeDbServer = ctx.getBean(AimeDbServer::class.java)
    aimeDbServer.start()

    // Start the AutoChecker
    val checker = ctx.getBean(AutoChecker::class.java)
    checker.check()
}

