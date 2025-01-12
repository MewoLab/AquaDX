package icu.samnyan.aqua.sega.maimai2.handler

import ext.div
import ext.isoDateTime
import ext.logger
import ext.path
import icu.samnyan.aqua.sega.general.BaseHandler
import icu.samnyan.aqua.sega.maimai2.model.request.UploadUserPhoto
import icu.samnyan.aqua.sega.util.jackson.BasicMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.io.IOException
import java.nio.file.Files
import java.nio.file.StandardOpenOption.APPEND
import java.nio.file.StandardOpenOption.CREATE
import java.time.LocalDateTime
import java.util.*

@Component("Maimai2UploadUserPhotoHandler")
class UploadUserPhotoHandler(private val mapper: BasicMapper) :
    BaseHandler {
    val tmpDir = "data/tmp".path().apply { toFile().mkdirs() }
    val uploadDir = "data/upload/mai2/plays".path().apply { toFile().mkdirs() }

    override fun handle(request: Map<String, Any>): String {
        // Maimai DX sends split base64 data for one jpeg image.
        // So, make a temp file and keep append bytes until last part received.
        // If finished, rename it to other name so user can keep save multiple scorecards in a single day.

        val uploadUserPhoto = mapper.convert(request, UploadUserPhoto::class.java)
        val up = uploadUserPhoto.userPhoto

        try {
            val tmpFile = tmpDir / "${up.userId}-${up.trackNo}.tmp"

            Files.write(tmpFile, Base64.getDecoder().decode(up.divData), CREATE, APPEND)

            if (up.divNumber == (up.divLength - 1))
                Files.move(tmpFile, uploadDir / "${up.userId}-${LocalDateTime.now().isoDateTime()}.jpg")
        } catch (e: IOException) {
            logger.error("Result: User photo save failed", e)
        }

        logger.info("Result: User photo saved")

        return """{"returnCode":1,"apiName":"com.sega.maimai2servlet.api.UploadUserPhotoApi"}"""
    }

    companion object {
        private val logger = logger()
    }
}
