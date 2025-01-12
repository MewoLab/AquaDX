package icu.samnyan.aqua.sega.general

import ext.*
import icu.samnyan.aqua.net.db.AquaUserServices
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class GameMusicPopularity(val us: AquaUserServices) {
    companion object {
        val log = logger()

        const val LOOK_BACK_DAYS: Long = 7
        const val QUERY_LIMIT: Long = 50
    }

    data class PopularMusic(val musicId: Int, val weight: Long)
    var ranking: Map<Str, List<PopularMusic>> = mapOf()

    @Scheduled(fixedDelay = 3600_000)
    fun refreshMusicRanking() {
        // Get the play count of each music in the last N days
        val after = LocalDate.now().minusDays(LOOK_BACK_DAYS).isoDate()

        ranking = ls("chusan", "ongeki").associateWith { game ->
            us.em.createNativeQuery("""
                SELECT music_id, count(user_id) as count 
                FROM ${game}_user_playlog_view
                WHERE user_play_date >= '${after}'
                GROUP BY music_id ORDER BY count DESC LIMIT ${QUERY_LIMIT};
            """.trimIndent()).exec.map { PopularMusic(it[0]!!.int, it[1]!!.long) }
                .also { log.info("Refreshed music ranking cache for $game: ${it.size} items") }
        }
    }
}