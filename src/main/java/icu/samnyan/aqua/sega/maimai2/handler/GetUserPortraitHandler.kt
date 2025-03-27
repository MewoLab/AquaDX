package icu.samnyan.aqua.sega.maimai2.handler

import ext.invoke
import ext.logger
import icu.samnyan.aqua.net.utils.PathProps
import icu.samnyan.aqua.sega.general.BaseHandler
import icu.samnyan.aqua.sega.general.dao.CardRepository
import icu.samnyan.aqua.sega.maimai2.model.request.Mai2UserPortrait
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.crypto.codec.Utf8
import org.springframework.stereotype.Component
import java.io.FileInputStream
import java.nio.file.Paths
import java.util.*

@Component("Maimai2GetUserPortraitHandler")
class GetUserPortraitHandler(
    val cardRepo: CardRepository,

    @param:Value("\${game.maimai2.userPhoto.enable:true}") val enable: Boolean,

    paths: PathProps
) : BaseHandler {
    val portraitPath = paths.aquaNetPortrait
    val log = logger()

    override fun handle(request: Map<String, Any>): Any? {
        if (!enable) return """{"length":0,"userPortraitList":[]}"""

        val uid = (request["userId"] as Number).toLong()
        val list = ArrayList<Mai2UserPortrait>()
        val profilePicture = cardRepo.findByExtId(uid)()?.aquaUser?.profilePicture?.ifBlank { null }
            ?: return """{"length":0,"userPortraitList":[]}"""

        try {
            val filePath = Paths.get(portraitPath, profilePicture)
            val buffer = ByteArray(10240)

            FileInputStream(filePath.toFile()).use { stream ->
                while (stream.available() > 0) {
                    val read = stream.read(buffer, 0, 10240)
                    val buf = if (read == 10240) buffer else Arrays.copyOfRange(buffer, 0, read)

                    list.add(Mai2UserPortrait().apply {
                        userId = uid
                        divData = Utf8.decode(Base64.getEncoder().encode(buf))
                        divNumber = list.size
                    })
                }
            }

            list.forEach { it.divLength = list.size }

            return mapOf(
                "length" to list.size,
                "userPortraitList" to list
            )
        } catch (e: Exception) {
            log.error("Result: User photo get failed", e)
            return """{"length":0,"userPortraitList":[]}"""
        }
    }
}
