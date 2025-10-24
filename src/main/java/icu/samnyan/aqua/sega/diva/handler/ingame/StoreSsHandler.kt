package icu.samnyan.aqua.sega.diva.handler.ingame

import ext.logger
import icu.samnyan.aqua.sega.diva.PlayerScreenShotRepository
import icu.samnyan.aqua.sega.diva.ProfileNotFoundException
import icu.samnyan.aqua.sega.diva.handler.BaseHandler
import icu.samnyan.aqua.sega.diva.model.request.ingame.StoreSsRequest
import icu.samnyan.aqua.sega.diva.model.response.BaseResponse
import icu.samnyan.aqua.sega.diva.model.userdata.PlayerScreenShot
import icu.samnyan.aqua.sega.diva.service.PlayerProfileService
import icu.samnyan.aqua.sega.diva.util.DivaStringUtils
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.function.Supplier

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
class StoreSsHandler(
    private val playerProfileService: PlayerProfileService,
    private val screenShotRepository: PlayerScreenShotRepository
) : BaseHandler() {
    val logger = logger()
    fun handle(request: StoreSsRequest, file: MultipartFile): Any {
        val profile = playerProfileService.findByPdId(request.pd_id).orElseThrow<ProfileNotFoundException?>(
            Supplier { ProfileNotFoundException() })

        var response: BaseResponse?
        try {
            val filename =
                request.pd_id.toString() + "-" + LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) + ".jpg"
            Files.write(Paths.get("data/" + filename), file.bytes)

            val ss = PlayerScreenShot(
                profile,
                filename,
                request.pd_id,
                DivaStringUtils.arrToCsv(request.ss_mdl_id),
                DivaStringUtils.arrToCsv(request.ss_c_itm_id)
            )
            screenShotRepository.save<PlayerScreenShot?>(ss)

            return BaseResponse(
                request.cmd,
                request.req_id,
                "ok"
            )
        } catch (e: IOException) {
            logger.error("Screenshot save failed", e)

            return BaseResponse(
                request.cmd,
                request.req_id,
                "0"
            )
        }
    }
}
