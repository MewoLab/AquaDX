package icu.samnyan.aqua.sega.diva.handler.user

import icu.samnyan.aqua.sega.diva.DIVA_OK
import icu.samnyan.aqua.sega.diva.DivaRepos
import icu.samnyan.aqua.sega.diva.model.common.ContestBorder
import icu.samnyan.aqua.sega.diva.model.common.Difficulty
import icu.samnyan.aqua.sega.diva.model.common.Edition
import icu.samnyan.aqua.sega.diva.model.common.SortMode
import icu.samnyan.aqua.sega.diva.model.gamedata.Contest
import icu.samnyan.aqua.sega.diva.model.request.ingame.StageResultRequest
import icu.samnyan.aqua.sega.diva.model.userdata.PlayerContest
import icu.samnyan.aqua.sega.diva.util.DivaStringUtils
import org.springframework.stereotype.Component
import java.lang.String
import java.time.LocalDateTime
import java.util.function.Supplier
import kotlin.Any
import kotlin.Int
import kotlin.math.max

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
class EndHandler(val db: DivaRepos) {
    fun handle(request: StageResultRequest): Any {
        val (profile, session) = db.session(request.pd_id)

        profile.headphoneVolume = request.getHp_vol()
        profile.isButtonSeOn = request.isBtn_se_vol
        profile.buttonSeVolume = request.getBtn_se_vol2()
        profile.sliderSeVolume = request.getSldr_se_vol2()
        profile.vocaloidPoints = session.vp
        profile.level = session.levelNumber
        profile.levelExp = session.levelExp
        profile.nextPvId = request.getNxt_pv_id()
        profile.nextDifficulty = Difficulty.fromValue(request.getNxt_dffclty())
        profile.nextEdition = Edition.fromValue(request.getNxt_edtn())
        profile.sortMode = SortMode.fromValue(request.getSort_kind())

        if (request.getCr_cid() != -1) {
            val contest = db.g.contest.findById(request.getCr_cid()).orElseGet(Supplier { Contest() })
            val currentResultRank = getContestRank(contest, request.getCr_tv())
            if (request.getCr_if() == 0) {
                // Do contest is playing
                profile.isContestNowPlayingEnable = true
                profile.contestNowPlayingId = request.getCr_cid()
                profile.contestNowPlayingResultRank = currentResultRank
                profile.contestNowPlayingValue = request.getCr_tv()
                profile.contestNowPlayingSpecifier = String.join(",", *request.getCr_sp())
            } else {
                val contestRecord =
                    db.contest.findByPdIdAndContestId(profile, request.getCr_cid()).orElseGet(
                        Supplier { PlayerContest(profile, request.getCr_cid()) })
                contestRecord.startCount += 1
                contestRecord.bestValue = max(contestRecord.bestValue, request.getCr_tv())
                contestRecord.resultRank = if (currentResultRank.value > contestRecord.resultRank
                        .value
                ) currentResultRank else contestRecord.resultRank
                contestRecord.lastUpdateTime = LocalDateTime.now()

                db.contest.save(contestRecord)
                profile.isContestNowPlayingEnable = false
                profile.contestNowPlayingId = -1
                profile.contestNowPlayingResultRank = ContestBorder.NONE
                profile.contestNowPlayingValue = -1
                profile.contestNowPlayingSpecifier = DivaStringUtils.getDummyString("-1", 60)
            }
        }

        db.profile.save(profile)
        db.gameSession.delete(session)

        return DIVA_OK
    }

    private fun getContestRank(contest: Contest, value: Int): ContestBorder {
        if (value >= contest.goldBorders) return ContestBorder.GOLD
        if (value >= contest.sliverBorders) return ContestBorder.SILVER
        if (value >= contest.bronzeBorders) return ContestBorder.BRONZE
        return ContestBorder.NONE
    }
}
