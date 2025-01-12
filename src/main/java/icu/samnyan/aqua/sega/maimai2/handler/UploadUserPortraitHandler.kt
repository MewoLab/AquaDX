package icu.samnyan.aqua.sega.maimai2.handler

import ext.div
import ext.logger
import ext.path
import icu.samnyan.aqua.net.utils.PathProps
import icu.samnyan.aqua.sega.general.BaseHandler
import icu.samnyan.aqua.sega.maimai2.model.request.UploadUserPortrait
import icu.samnyan.aqua.sega.util.jackson.BasicMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.IOException
import java.nio.file.Files
import java.nio.file.StandardCopyOption.REPLACE_EXISTING
import java.nio.file.StandardOpenOption.APPEND
import java.nio.file.StandardOpenOption.CREATE
import java.util.*
import kotlin.io.path.writeText

@Component("Maimai2UploadUserPortraitHandler")
class UploadUserPortraitHandler(
    val mapper: BasicMapper,
    paths: PathProps,
    @param:Value("\${game.maimai2.userPhoto.enable:true}") val enable: Boolean,
    @param:Value("\${game.maimai2.userPhoto.divMaxLength:32}") val divMaxLength: Long
) : BaseHandler {
    val path = paths.mai2Portrait.path()
    val success = """{"returnCode":1,"apiName":"com.sega.maimai2servlet.api.UploadUserPortraitApi"}"""
    val fail = """{"returnCode":-1,"apiName":"com.sega.maimai2servlet.api.UploadUserPortraitApi"}"""

    override fun handle(request: Map<String, Any>): String {
        if (!enable) return success

        // Maimai DX sends split base64 data for one jpeg image.
        // So, make a temp file and keep append bytes until last part received.
        // If finished, rename it to other name so user can keep save multiple scorecards in a single day.

        val up = mapper.convert(request, UploadUserPortrait::class.java).userPortrait

        val id = up.userId
        val num = up.divNumber
        val len = up.divLength

        if (len > divMaxLength) {
            logger.warn("stop user $id uploading photo data because divLength($len) > divMaxLength($divMaxLength)")
            return fail
        }

        try {
            val tmp = path / "$id-up.tmp"
            if (num == 0) Files.deleteIfExists(tmp)
            Files.write(tmp, Base64.getDecoder().decode(up.divData), CREATE, APPEND)

            logger.info("> User photo $id data ${num + 1}/${len}")

            if (num == (len - 1)) {
                Files.move(tmp, path / "$id-up.jpg", REPLACE_EXISTING)

                up.divData = ""
                (path / "$id-up.json").writeText(mapper.write(up))

                logger.info("> Saved user $id photo data")
            }
        } catch (e: IOException) {
            logger.error("> User photo save failed", e)
        }

        return success
    }

    companion object {
        private val logger = logger()
    }
}
