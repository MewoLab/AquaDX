package icu.samnyan.aqua.sega.maimai2.handler

import ext.logger
import ext.millis
import icu.samnyan.aqua.sega.allnet.TokenChecker
import icu.samnyan.aqua.sega.general.BaseHandler
import icu.samnyan.aqua.sega.maimai2.model.Mai2UserDataRepo
import icu.samnyan.aqua.sega.maimai2.model.Mai2UserPlaylogRepo
import icu.samnyan.aqua.sega.maimai2.model.request.UploadUserPlaylog
import icu.samnyan.aqua.sega.maimai2.model.userdata.Mai2UserPlaylog
import icu.samnyan.aqua.sega.util.jackson.BasicMapper
import icu.samnyan.aqua.spring.Metrics
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import kotlin.jvm.optionals.getOrNull

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component("Maimai2UploadUserPlaylogHandler")
class UploadUserPlaylogHandler(
    private val userDataRepository: Mai2UserDataRepo,
    private val playlogRepo: Mai2UserPlaylogRepo,
    private val mapper: BasicMapper
) : BaseHandler {
    data class BacklogEntry(val time: Long, val playlog: Mai2UserPlaylog)
    companion object {
        @JvmStatic
        val playBacklog = mutableMapOf<Long, MutableList<BacklogEntry>>()

        val VALID_GAME_IDS = setOf("SDEZ", "SDGA", "SDGB")
        val log = logger()
    }

    override fun handle(request: Map<String, Any>): String {
        val req = mapper.convert(request, UploadUserPlaylog::class.java)

        val version = tryParseGameVersion(req.userPlaylog.version)
        if (version != null) {
            val session = TokenChecker.getCurrentSession()
            val gameId = if (session?.gameId in VALID_GAME_IDS) session!!.gameId else ""
            Metrics.counter(
                "aquadx_maimai2_playlog_game_version",
                "game_id" to gameId, "version" to version
            ).increment()
        }

        // Check duplicate
        val isDup = playlogRepo.findByUser_Card_ExtIdAndMusicIdAndUserPlayDate(
            req.userId,
            req.userPlaylog.musicId,
            req.userPlaylog.userPlayDate
        ).size > 0
        if (isDup) {
            log.info("Duplicate playlog detected")
            return """{"returnCode":1,"apiName":"com.sega.maimai2servlet.api.UploadUserPlaylogApi"}"""
        }

        // Save if the user is registered
        val u = userDataRepository.findByCardExtId(req.userId).getOrNull()
        if (u != null) playlogRepo.save(req.userPlaylog.apply { user = u })

        // If the user hasn't registered (first play), save the playlog to a backlog
        else {
            playBacklog.putIfAbsent(req.userId, mutableListOf())
            playBacklog[req.userId]?.apply {
                add(BacklogEntry(millis(), req.userPlaylog))
                if (size > 6) clear()  // Prevent abuse
            }
        }

        return """{"returnCode":1,"apiName":"com.sega.maimai2servlet.api.UploadUserPlaylogApi"}"""
    }

    @Scheduled(fixedDelay = 60_000)
    fun cleanBacklog() {
        // Clean all backlog entries that are older than 5 minutes
        val now = millis()
        playBacklog.filter { (_, v) -> v.isEmpty() || v[0].time - now > 300_000 }.toList()
            .forEach { (k, _) -> playBacklog.remove(k) }
    }

    private fun tryParseGameVersion(version: Int): String? {
        val major = version / 1000000
        val minor = version / 1000 % 1000
        if (major != 1) return null
        if (minor !in 0..99) return null
        // e.g. "1.30", minor should have two digits
        return "$major.${minor.toString().padStart(2, '0')}"
    }
}
