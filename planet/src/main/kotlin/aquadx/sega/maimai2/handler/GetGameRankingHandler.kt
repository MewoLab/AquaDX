package aquadx.sega.maimai2.handler

import aquadx.sega.maimai2.model.Mai2UserPlaylogRepo
import ext.logger
import ext.thread
import aquadx.sega.allnet.TokenChecker
import aquadx.sega.general.BaseHandler
import org.springframework.data.domain.PageRequest
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
    private val playlogRepo: Mai2UserPlaylogRepo
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

        musicRankingCache = playlogRepo.getMusicRanking(queryAfterStr, PageRequest.of(0, QUERY_LIMIT.toInt()))
            .map { MusicRankingItem(it.musicId, it.userCount) }

        log.info("Refreshed music ranking cache: ${musicRankingCache.size} items")
    }

    override fun handle(request: Map<String, Any>): Any = mapOf(
        "type" to request["type"],
        "gameRankingList" to when(request["type"]) {
            1 -> {
                val opts = TokenChecker.getCurrentSession()?.user?.gameOptions
                // If is null or true, return the ranking list
                if (opts?.enableMusicRank == false)
                    emptyList()
                else
                    musicRankingCache.map { mapOf("id" to it.musicId, "point" to it.weight, "userName" to "") }
            }
            else -> emptyList()
        }
    )

    companion object {
        val log = logger()
        
        const val LOOK_BACK_DAYS: Long = 7
        const val QUERY_LIMIT: Long = 50
    }
}
