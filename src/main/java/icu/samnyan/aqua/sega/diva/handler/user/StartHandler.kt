package icu.samnyan.aqua.sega.diva.handler.user

import icu.samnyan.aqua.sega.diva.DivaRepos
import icu.samnyan.aqua.sega.diva.model.StartRequest
import icu.samnyan.aqua.sega.diva.model.StartResponse
import icu.samnyan.aqua.sega.diva.model.common.*
import icu.samnyan.aqua.sega.diva.model.common.collection.ClearTally
import icu.samnyan.aqua.sega.diva.model.db.userdata.GameSession
import icu.samnyan.aqua.sega.diva.model.db.userdata.PlayerContest
import icu.samnyan.aqua.sega.diva.model.db.userdata.PlayerProfile
import icu.samnyan.aqua.sega.diva.model.db.userdata.PlayerPvRecord
import icu.samnyan.aqua.sega.diva.util.PvRecordDataException
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*
import java.util.function.Consumer
import java.util.stream.Collectors

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
class StartHandler(val db: DivaRepos) {
    fun handle(request: StartRequest): Any {
        val (profile, session) = db.session(request.pd_id)

        session.startMode = StartMode.START
        db.gameSession.save<GameSession>(session)

        val module_have = db.s.module.getModuleHaveString(profile)
        val customize_have = db.s.customize.getModuleHaveString(profile)

        val contestResult = getContestResult(profile)

        var border = if (profile.showGreatBorder) 1 else 0
        border = border or ((if (profile.showExcellentBorder) 1 else 0) shl 1)
        border = border or ((if (profile.showRivalBorder) 1 else 0) shl 2)

        return StartResponse(
            profile.pdId,
            Result.SUCCESS,
            session.acceptId,
            session.acceptId,
            profile.playerName,
            profile.headphoneVolume,
            profile.buttonSeOn,
            profile.buttonSeVolume,
            profile.sliderSeVolume,
            profile.sortMode,
            profile.level,
            profile.levelExp,
            profile.levelTitle,
            profile.plateEffectId,
            profile.plateId,
            profile.commonModule,
            profile.commonCustomizeItems,
            profile.moduleSelectItemFlag,
            LocalDateTime.now(),
            module_have,
            customize_have,
            profile.preferPerPvModule,
            profile.preferCommonModule,
            profile.usePerPvSkin,
            profile.usePerPvButtonSe,
            profile.usePerPvSliderSe,
            profile.usePerPvChainSliderSe,
            profile.usePerPvTouchSliderSe,
            profile.vocaloidPoints,
            profile.nextPvId,
            profile.nextDifficulty,
            profile.nextEdition,
            contestResult["cv_cid"],  // contest progress
            contestResult["cv_sc"],
            contestResult["cv_rr"],
            contestResult["cv_bv"],
            contestResult["cv_bf"],
            if (profile.contestNowPlayingEnable) profile.contestNowPlayingId else -1,
            profile.contestNowPlayingValue,
            profile.contestNowPlayingResultRank,
            profile.contestNowPlayingSpecifier,
            profile.myList0,
            profile.myList1,
            profile.myList2,
            null,
            null,
            border.toString(),
            profile.showInterimRanking,
            profile.showClearStatus,
            countClearStatus(profile),
            profile.showRgoSetting,
            null,  // Currently quest not working
            null,
            null,
            null,
            null,
            null
        )
    }

    private fun countClearStatus(profile: PlayerProfile): String {
        val pvRecordList = db.pvRecord.findByPdId(profile)
        val clearTally = ClearTally()
        pvRecordList.forEach(Consumer { x: PlayerPvRecord ->
            when (x.edition) {
                Edition.ORIGINAL -> {
                    when (x.result) {
                        ClearResult.CHEAP -> getDiff(x, clearTally).addClear()
                        ClearResult.STANDARD -> getDiff(x, clearTally).addClear()
                        ClearResult.GREAT -> getDiff(x, clearTally).addGreat()
                        ClearResult.EXCELLENT -> getDiff(x, clearTally).addExcellent()
                        ClearResult.PERFECT -> getDiff(x, clearTally).addPerfect()
                        else -> {}
                    }
                }

                Edition.EXTRA -> {
                    when (x.result) {
                        ClearResult.CHEAP -> clearTally.extraExtreme.addClear()
                        ClearResult.STANDARD -> clearTally.extraExtreme.addClear()
                        ClearResult.GREAT -> clearTally.extraExtreme.addGreat()
                        ClearResult.EXCELLENT -> clearTally.extraExtreme.addExcellent()
                        ClearResult.PERFECT -> clearTally.extraExtreme.addPerfect()
                        else -> {}
                    }
                }
            }
        })
        return clearTally.toInternal()
    }

    private fun getDiff(record: PlayerPvRecord, clearTally: ClearTally) = when (record.difficulty) {
        Difficulty.EASY -> clearTally.easy
        Difficulty.NORMAL -> clearTally.normal
        Difficulty.HARD -> clearTally.hard
        Difficulty.EXTREME -> clearTally.extreme
        else -> throw PvRecordDataException("Difficulty data not exist, record id:" + record.id)
    }

    private fun getContestResult(profile: PlayerProfile): MutableMap<String, String> {
        val cv_cid: MutableList<Int> = LinkedList<Int>()
        val cv_sc: MutableList<Int> = LinkedList<Int>()
        val cv_rr: MutableList<Int> = LinkedList<Int>()
        val cv_bv: MutableList<Int> = LinkedList<Int>()
        val cv_bf: MutableList<Int> = LinkedList<Int>()
        val contestList = db.contest.findTop4ByPdIdOrderByLastUpdateTimeDesc(profile)
        contestList.forEach(Consumer { x: PlayerContest ->
            cv_cid.add(x.contestId)
            cv_sc.add(x.startCount)
            cv_rr.add(x.resultRank.value)
            cv_bv.add(x.bestValue)
            cv_bf.add(-1)
        })
        for (i in cv_cid.size..3) {
            cv_cid.add(-1)
            cv_sc.add(-1)
            cv_rr.add(-1)
            cv_bv.add(-1)
            cv_bf.add(-1)
        }
        val result: MutableMap<String, String> = HashMap<String, String>()
        result["cv_cid"] = cv_cid.stream().map { it.toString() }.collect(Collectors.joining(","))
        result["cv_sc"] = cv_sc.stream().map { it.toString() }.collect(Collectors.joining(","))
        result["cv_rr"] = cv_rr.stream().map { it.toString() }.collect(Collectors.joining(","))
        result["cv_bv"] = cv_bv.stream().map { it.toString() }.collect(Collectors.joining(","))
        result["cv_bf"] = cv_bf.stream().map { it.toString() }.collect(Collectors.joining(","))
        return result
    }
}
