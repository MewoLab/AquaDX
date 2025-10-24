package icu.samnyan.aqua.sega.diva.handler.databank

import icu.samnyan.aqua.sega.diva.PlayerPvRecordRepository
import icu.samnyan.aqua.sega.diva.handler.BaseHandler
import icu.samnyan.aqua.sega.diva.model.common.Difficulty
import icu.samnyan.aqua.sega.diva.model.common.Edition
import icu.samnyan.aqua.sega.diva.model.common.collection.PsRankingCollection
import icu.samnyan.aqua.sega.diva.model.request.databank.PsRankingRequest
import icu.samnyan.aqua.sega.diva.model.response.databank.PsRankingResponse
import icu.samnyan.aqua.sega.diva.util.URIEncoder.encode
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*
import java.util.stream.Collectors

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
class PsRankingHandler(private val playerPvRecordRepository: PlayerPvRecordRepository) : BaseHandler() {
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
            val records = playerPvRecordRepository.findTop3ByPvIdAndEditionAndDifficultyOrderByMaxScoreDesc(
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
            name1.add(
                encode(
                    if (obj.first.pdId != null) obj.first.pdId.playerName else "xxx"
                )
            )
            name2.add(
                encode(
                    if (obj.second.pdId != null) obj.second.pdId.playerName else "xxx"
                )
            )
            name3.add(
                encode(
                    if (obj.third.pdId != null) obj.third.pdId.playerName else "xxx"
                )
            )
        }

        return PsRankingResponse(
            request.cmd,
            request.req_id,
            "ok",
            LocalDateTime.now(),
            LocalDateTime.now(),
            request.rnk_ps_idx,
            pvIds.stream().map<String?> { obj: Int? -> obj.toString() }.collect(Collectors.joining(",")),
            edition1.stream().map<String?> { obj: Int? -> obj.toString() }.collect(Collectors.joining(",")),
            edition2.stream().map<String?> { obj: Int? -> obj.toString() }.collect(Collectors.joining(",")),
            edition3.stream().map<String?> { obj: Int? -> obj.toString() }.collect(Collectors.joining(",")),
            score1.stream().map<String?> { obj: Int? -> obj.toString() }.collect(Collectors.joining(",")),
            score2.stream().map<String?> { obj: Int? -> obj.toString() }.collect(Collectors.joining(",")),
            score3.stream().map<String?> { obj: Int? -> obj.toString() }.collect(Collectors.joining(",")),
            name1.stream().map<String?> { obj: String? -> obj.toString() }.collect(Collectors.joining(",")),
            name2.stream().map<String?> { obj: String? -> obj.toString() }.collect(Collectors.joining(",")),
            name3.stream().map<String?> { obj: String? -> obj.toString() }.collect(Collectors.joining(","))
        )
    }
}
