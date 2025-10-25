package icu.samnyan.aqua.sega.diva.handler.databank

import ext.csv
import icu.samnyan.aqua.sega.diva.DivaRepos
import icu.samnyan.aqua.sega.diva.model.PsRankingRequest
import icu.samnyan.aqua.sega.diva.model.PsRankingResponse
import icu.samnyan.aqua.sega.diva.model.common.Difficulty
import icu.samnyan.aqua.sega.diva.model.common.Edition
import icu.samnyan.aqua.sega.diva.model.common.collection.PsRankingCollection
import icu.samnyan.aqua.sega.diva.util.URIEncoder.encode
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
class PsRankingHandler(val db: DivaRepos) {
    fun handle(request: PsRankingRequest): Any {
        var edition = Edition.ORIGINAL
        var difficulty = Difficulty.HARD

        when (request.rnk_ps_idx) {
            0 -> difficulty = Difficulty.HARD
            1 -> difficulty = Difficulty.EXTREME
            2 -> {
                difficulty = Difficulty.EXTREME
                edition = Edition.EXTRA
            }
        }

        val list = request.rnk_ps_pv_id_lst
        val resultCollections: MutableMap<Int?, PsRankingCollection?> = LinkedHashMap<Int?, PsRankingCollection?>()
        for (i in list) {
            val records = db.pvRecord.findTop3ByPvIdAndEditionAndDifficultyOrderByMaxScoreDesc(
                i,
                edition,
                difficulty
            )
            resultCollections.put(i, PsRankingCollection(i, edition, records))
        }

        val pvIds: MutableList<Int?> = LinkedList<Int?>()
        val edition1: MutableList<Int?> = LinkedList<Int?>()
        val edition2: MutableList<Int?> = LinkedList<Int?>()
        val edition3: MutableList<Int?> = LinkedList<Int?>()
        val score1: MutableList<Int?> = LinkedList<Int?>()
        val score2: MutableList<Int?> = LinkedList<Int?>()
        val score3: MutableList<Int?> = LinkedList<Int?>()
        val name1: MutableList<String?> = LinkedList<String?>()
        val name2: MutableList<String?> = LinkedList<String?>()
        val name3: MutableList<String?> = LinkedList<String?>()

        resultCollections.forEach { (key: Int?, obj: PsRankingCollection?) ->
            pvIds.add(key)
            edition1.add(obj!!.first.edition.value)
            edition2.add(obj.second.edition.value)
            edition3.add(obj.third.edition.value)
            score1.add(obj.first.maxScore)
            score2.add(obj.second.maxScore)
            score3.add(obj.third.maxScore)
            name1.add(encode(obj.first.pdId?.playerName ?: "xxx"))
            name2.add(encode(obj.second.pdId?.playerName ?: "xxx"))
            name3.add(encode(obj.third.pdId?.playerName ?: "xxx"))
        }

        return PsRankingResponse(
            LocalDateTime.now(),
            LocalDateTime.now(),
            request.rnk_ps_idx,
            pvIds.csv,
            edition1.csv,
            edition2.csv,
            edition3.csv,
            score1.csv,
            score2.csv,
            score3.csv,
            name1.csv,
            name2.csv,
            name3.csv
        )
    }
}
