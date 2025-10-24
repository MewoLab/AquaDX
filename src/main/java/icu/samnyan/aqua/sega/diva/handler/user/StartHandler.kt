package icu.samnyan.aqua.sega.diva.handler.user

import icu.samnyan.aqua.sega.diva.*
import icu.samnyan.aqua.sega.diva.handler.BaseHandler
import icu.samnyan.aqua.sega.diva.model.common.*
import icu.samnyan.aqua.sega.diva.model.common.collection.ClearSet
import icu.samnyan.aqua.sega.diva.model.common.collection.ClearTally
import icu.samnyan.aqua.sega.diva.model.request.user.StartRequest
import icu.samnyan.aqua.sega.diva.model.response.user.StartResponse
import icu.samnyan.aqua.sega.diva.model.userdata.GameSession
import icu.samnyan.aqua.sega.diva.model.userdata.PlayerContest
import icu.samnyan.aqua.sega.diva.model.userdata.PlayerProfile
import icu.samnyan.aqua.sega.diva.model.userdata.PlayerPvRecord
import icu.samnyan.aqua.sega.diva.service.PlayerCustomizeService
import icu.samnyan.aqua.sega.diva.service.PlayerModuleService
import icu.samnyan.aqua.sega.diva.service.PlayerProfileService
import icu.samnyan.aqua.sega.diva.util.ProfileNotFoundException
import icu.samnyan.aqua.sega.diva.util.PvRecordDataException
import icu.samnyan.aqua.sega.diva.util.SessionNotFoundException
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*
import java.util.function.Consumer
import java.util.function.Supplier
import java.util.stream.Collectors

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
class StartHandler(
    private val playerProfileService: PlayerProfileService,
    private val gameSessionRepository: GameSessionRepository,
    private val playerCustomizeService: PlayerCustomizeService,
    private val playerModuleService: PlayerModuleService,
    private val playerPvRecordRepository: PlayerPvRecordRepository,
    private val playerContestRepository: PlayerContestRepository
) : BaseHandler() {
    fun handle(request: StartRequest): Any {
        val profile = playerProfileService.findByPdId(request.getPd_id()).orElseThrow<ProfileNotFoundException>(
            Supplier { ProfileNotFoundException() })
        val session = gameSessionRepository.findByPdId(profile)
            .orElseThrow(Supplier { SessionNotFoundException() })

        session.startMode = StartMode.START
        gameSessionRepository.save<GameSession>(session)

        val module_have = playerModuleService.getModuleHaveString(profile)
        val customize_have = playerCustomizeService.getModuleHaveString(profile)

        val contestResult = getContestResult(profile)

        var border = if (profile.isShowGreatBorder) 1 else 0
        border = border or ((if (profile.isShowExcellentBorder) 1 else 0) shl 1)
        border = border or ((if (profile.isShowRivalBorder) 1 else 0) shl 2)

        return StartResponse(
            request.cmd,
            request.req_id,
            "ok",
            profile.pdId,
            Result.SUCCESS,
            session.acceptId,
            session.acceptId,
            profile.playerName,
            profile.headphoneVolume,
            profile.isButtonSeOn,
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
            profile.isPreferPerPvModule,
            profile.isPreferCommonModule,
            profile.isUsePerPvSkin,
            profile.isUsePerPvButtonSe,
            profile.isUsePerPvSliderSe,
            profile.isUsePerPvChainSliderSe,
            profile.isUsePerPvTouchSliderSe,
            profile.vocaloidPoints,
            profile.nextPvId,
            profile.nextDifficulty,
            profile.nextEdition,
            contestResult.get("cv_cid"),  // contest progress
            contestResult.get("cv_sc"),
            contestResult.get("cv_rr"),
            contestResult.get("cv_bv"),
            contestResult.get("cv_bf"),
            if (profile.isContestNowPlayingEnable) profile.contestNowPlayingId else -1,
            profile.contestNowPlayingValue,
            profile.contestNowPlayingResultRank,
            profile.contestNowPlayingSpecifier,
            profile.myList0,
            profile.myList1,
            profile.myList2,
            null,
            null,  //                getDummyString("-1", 40),
            //                getDummyString("-1", 40),
            border.toString(),
            profile.isShowInterimRanking,
            profile.isShowClearStatus,
            countClearStatus(profile),
            profile.isShowRgoSetting,
            null,  // Currently quest not working
            null,
            null,
            null,
            null,
            null
        )
    }

    private fun countClearStatus(profile: PlayerProfile): String {
        val pvRecordList = playerPvRecordRepository.findByPdId(profile)
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

    private fun getDiff(record: PlayerPvRecord, clearTally: ClearTally): ClearSet {
        when (record.difficulty) {
            Difficulty.EASY -> return clearTally.easy
            Difficulty.NORMAL -> return clearTally.normal
            Difficulty.HARD -> return clearTally.hard
            Difficulty.EXTREME -> return clearTally.extreme
            else -> throw PvRecordDataException("Difficulty data not exist, record id:" + record.id)
        }
    }

    private fun getContestResult(profile: PlayerProfile): MutableMap<String, String> {
        val cv_cid: MutableList<Int> = LinkedList<Int>()
        val cv_sc: MutableList<Int> = LinkedList<Int>()
        val cv_rr: MutableList<Int> = LinkedList<Int>()
        val cv_bv: MutableList<Int> = LinkedList<Int>()
        val cv_bf: MutableList<Int> = LinkedList<Int>()
        val contestList = playerContestRepository.findTop4ByPdIdOrderByLastUpdateTimeDesc(profile)
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
