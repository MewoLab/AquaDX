package icu.samnyan.aqua.sega.maimai2.handler

import com.querydsl.jpa.impl.JPAQueryFactory
import ext.logger
import ext.thread
import icu.samnyan.aqua.sega.general.BaseHandler
import icu.samnyan.aqua.sega.maimai2.model.userdata.QMai2UserPlaylog
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.concurrent.Volatile

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component("Maimai2GetGameRankingHandler")
class GetGameRankingHandler(
    private val queryFactory: JPAQueryFactory
) : BaseHandler {
    private data class MusicRankingItem(val musicId: Int, val weight: Long)

    @Volatile
    private var musicRankingCache: List<MusicRankingItem> = emptyList()

    init {
        // To make sure the cache is initialized before the first request,
        // not using `initialDelay = 0` in `@Scheduled`.
        thread { refreshMusicRankingCache() }
    }

    @Scheduled(fixedDelay = 3600_000)
    private fun refreshMusicRankingCache() {
        // Get the play count of each music in the last N days
        val queryAfter = LocalDateTime.now().minusDays(LOOK_BACK_DAYS)
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val queryAfterStr = queryAfter.format(formatter)

        val qPlaylog = QMai2UserPlaylog.mai2UserPlaylog
        val cMusicId = qPlaylog.musicId
        val cUserCount = qPlaylog.user.id.countDistinct()
        musicRankingCache = queryFactory
            .select(cMusicId, cUserCount)
            .from(qPlaylog)
            .where(qPlaylog.userPlayDate.stringValue().goe(queryAfterStr))
            .groupBy(cMusicId)
            .orderBy(cUserCount.desc())
            .limit(QUERY_LIMIT)
            .fetch()
            .map { MusicRankingItem(it.get(cMusicId)!!, it.get(cUserCount)!!) }

        log.info("Refreshed music ranking cache: ${musicRankingCache.size} items")
    }

    override fun handle(request: Map<String, Any>): Any = mapOf(
        "type" to request["type"],
        "gameRankingList" to when(request["type"]) {
            1 -> musicRankingCache.map { mapOf("id" to it.musicId, "point" to it.weight, "userName" to "") }
            else -> emptyList()
        }
    )

    companion object {
        val log = logger()
        
        const val LOOK_BACK_DAYS: Long = 7
        const val QUERY_LIMIT: Long = 50
    }
}
