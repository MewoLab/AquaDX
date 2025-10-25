package icu.samnyan.aqua.sega.diva.handler.ingame

import ext.csv
import ext.logger
import icu.samnyan.aqua.sega.diva.DIVA_BAD
import icu.samnyan.aqua.sega.diva.DIVA_OK
import icu.samnyan.aqua.sega.diva.DivaRepos
import icu.samnyan.aqua.sega.diva.model.StoreSsRequest
import icu.samnyan.aqua.sega.diva.model.db.userdata.PlayerScreenShot
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDateTime
import java.time.ZoneOffset

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
class StoreSsHandler(val db: DivaRepos) {
    val logger = logger()
    fun handle(request: StoreSsRequest, file: MultipartFile): Any {
        val profile = db.profile(request.pd_id)

        try {
            val filename = "${request.pd_id}-${LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)}.jpg"
            Files.write(Paths.get("data/$filename"), file.bytes)

            val ss = PlayerScreenShot(
                profile,
                filename,
                request.pd_id,
                request.ss_mdl_id.csv,
                request.ss_c_itm_id.csv
            )
            db.screenShot.save(ss)

            return DIVA_OK
        } catch (e: IOException) {
            logger.error("Screenshot save failed", e)
            return DIVA_BAD
        }
    }
}
