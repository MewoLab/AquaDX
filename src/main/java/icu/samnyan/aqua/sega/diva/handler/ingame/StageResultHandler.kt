package icu.samnyan.aqua.sega.diva.handler.ingame

import ext.logger
import icu.samnyan.aqua.sega.diva.ContestRepository
import icu.samnyan.aqua.sega.diva.GameSessionRepository
import icu.samnyan.aqua.sega.diva.PlayLogRepository
import icu.samnyan.aqua.sega.diva.PlayerContestRepository
import icu.samnyan.aqua.sega.diva.PlayerCustomizeRepository
import icu.samnyan.aqua.sega.diva.PlayerInventoryRepository
import icu.samnyan.aqua.sega.diva.PlayerPvRecordRepository
import icu.samnyan.aqua.sega.diva.util.ProfileNotFoundException
import icu.samnyan.aqua.sega.diva.util.SessionNotFoundException
import icu.samnyan.aqua.sega.diva.model.common.*
import icu.samnyan.aqua.sega.diva.model.request.ingame.StageResultRequest
import icu.samnyan.aqua.sega.diva.model.response.ingame.StageResultResponse
import icu.samnyan.aqua.sega.diva.model.userdata.*
import icu.samnyan.aqua.sega.diva.service.PlayerProfileService
import icu.samnyan.aqua.sega.diva.util.DivaCalculator
import org.apache.commons.lang3.StringUtils
import org.springframework.stereotype.Component
import java.lang.String
import java.time.LocalDateTime
import java.util.*
import java.util.function.Supplier
import kotlin.Any
import kotlin.Array
import kotlin.Int
import kotlin.IntArray
import kotlin.arrayOf
import kotlin.math.max

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
class StageResultHandler(
    private val gameSessionRepository: GameSessionRepository,
    private val pvRecordRepository: PlayerPvRecordRepository,
    private val playerProfileService: PlayerProfileService,
    private val playLogRepository: PlayLogRepository,
    private val contestRepository: ContestRepository,
    private val playerContestRepository: PlayerContestRepository,
    private val playerCustomizeRepository: PlayerCustomizeRepository,
    private val playerInventoryRepository: PlayerInventoryRepository,
    private val divaCalculator: DivaCalculator
) {
    private var currentProfile: PlayerProfile? = null
    val logger = logger()

    fun handle(request: StageResultRequest): Any {
        val response: StageResultResponse?
        if (request.getPd_id() != -1L) {
            val profile = playerProfileService.findByPdId(request.getPd_id()).orElseThrow<ProfileNotFoundException?>(
                Supplier { ProfileNotFoundException() })
            val session = gameSessionRepository.findByPdId(profile)
                .orElseThrow<SessionNotFoundException?>(Supplier { SessionNotFoundException() })

            currentProfile = profile
            // Get the last played index
            request.getStg_ply_pv_id()
            val stageArr = request.getStg_ply_pv_id()
            var stageIndex = 0
            if (stageArr[0] != -1) {
                stageIndex = 0
            }
            if (stageArr[1] != -1) {
                stageIndex = 1
            }
            if (stageArr[2] != -1) {
                stageIndex = 2
            }
            if (stageArr[3] != -1) {
                stageIndex = 3
            }

            // Convert to play log object
            val log = getLog(request, profile, stageIndex)
            logger.debug("Stage Result Object: {}", log.toString())

            val record = pvRecordRepository.findByPdIdAndPvIdAndEditionAndDifficulty(
                profile,
                log.pvId,
                log.edition,
                log.difficulty
            )
                .orElseGet(Supplier { PlayerPvRecord(profile, log.pvId, log.edition, log.difficulty) })

            // Not save personal record in no fail mode
            if (request.getGame_type() != 1) {
                // Only update personal record when using rhythm game option
                if (log.rhythmGameOptions == "0,0,0") {
                    // Update pvRecord field
                    record.maxScore = max(record.maxScore, log.score)
                    record.maxAttain = max(record.maxAttain, log.attainPoint)

                    if (record.result.value < log.clearResult.value) {
                        record.result = log.clearResult
                    }
                }
            }

            val updateRgo =
                log.rhythmGameOptions.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val oldRgo =
                record.rgoPlayed.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            for (i in updateRgo.indices) {
                if (updateRgo[i] == "1") {
                    oldRgo[i] = "1"
                }
            }
            record.rgoPlayed = StringUtils.join(oldRgo, ",")

            session.vp = session.vp + log.vp
            session.lastPvId = log.pvId
            session.lastUpdateTime = LocalDateTime.now()

            val levelInfo = divaCalculator.getLevelInfo(profile)
            session.oldLevelNumber = session.levelNumber
            session.oldLevelExp = session.levelExp
            session.levelNumber = levelInfo.levelNumber
            session.levelExp = levelInfo.levelExp

            session.stageResultIndex = stageIndex

            // Calculate reward
            // Contest reward
            var contestSpecifier = String.join(",", *request.getCr_sp())
            val contestRewardType = arrayOf<kotlin.String?>("-1", "-1", "-1")
            val contestRewardValue = arrayOf<kotlin.String?>("-1", "-1", "-1")
            val contestRewardString1 = arrayOf<kotlin.String?>("***", "***", "***")
            val contestRewardString2 = arrayOf<kotlin.String?>("***", "***", "***")
            var contestEntryRewardType = -1
            var contestEntryRewardValue = -1
            var contestEntryRewardString1: kotlin.String? = "***"
            var contestEntryRewardString2: kotlin.String? = "***"
            val contestId = request.getCr_cid()
            if (contestId != -1) {
                val progress = getContestProgress(request.getCr_sp())
                contestSpecifier = getContestSpecifier(progress)

                // Check if the contest info exist
                val contestOptional = contestRepository.findById(contestId)
                if (contestOptional.isPresent) {
                    val contest = contestOptional.get()
                    val playerContestOptional = playerContestRepository.findByPdIdAndContestId(profile, contestId)

                    // Contest Entry Reward
                    // Check if this is first stage
                    if (progress.size == 1 && playerContestOptional.isEmpty) {
                        if (StringUtils.isNotBlank(contest.contestEntryReward)) {
                            // Check if this is first time play this contest
                            val reward = contest.contestEntryReward
                            val rewardValue: Array<kotlin.String?> =
                                reward.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

                            contestEntryRewardType = rewardValue[0]!!.toInt()
                            contestEntryRewardValue = rewardValue[1]!!.toInt()
                            contestEntryRewardString1 = rewardValue[2]
                            contestEntryRewardString2 = rewardValue[3]
                        }
                    }

                    // Only if this is the first time reach this value
                    val previousValue = progress.stream().limit((progress.size - 1).toLong())
                        .mapToInt { obj: ContestProgress? -> obj!!.getScores() }.sum()
                    val currentValue = progress.stream().mapToInt { obj: ContestProgress? -> obj!!.getScores() }.sum()

                    // Bronze Reward
                    val bronze = updateReward(
                        currentValue,
                        previousValue,
                        contest.bronzeBorders,
                        contest.bronzeContestReward
                    )
                    if (bronze != null) {
                        contestRewardType[0] = bronze.get("type")
                        contestRewardValue[0] = bronze.get("value")
                        contestRewardString1[0] = bronze.get("string1")
                        contestRewardString2[2] = bronze.get("string2")
                    }

                    // Silver Reward
                    val silver = updateReward(
                        currentValue,
                        previousValue,
                        contest.sliverBorders,
                        contest.sliverContestReward
                    )
                    if (silver != null) {
                        contestRewardType[1] = silver.get("type")
                        contestRewardValue[1] = silver.get("value")
                        contestRewardString1[1] = silver.get("string1")
                        contestRewardString2[2] = silver.get("string2")
                    }

                    // Gold Reward
                    val gold = updateReward(
                        currentValue,
                        previousValue,
                        contest.goldBorders,
                        contest.goldContestReward
                    )
                    if (gold != null) {
                        contestRewardType[2] = gold.get("type")
                        contestRewardValue[2] = gold.get("value")
                        contestRewardString1[2] = gold.get("string1")
                        contestRewardString2[2] = gold.get("string2")
                    }
                }
            }

            pvRecordRepository.save<PlayerPvRecord?>(record)
            playLogRepository.save<PlayLog?>(log)
            gameSessionRepository.save<GameSession?>(session)


            return StageResultResponse(
                request.cmd,
                request.req_id,
                "ok",
                ChallengeKind.UNDEFINED.value,
                session.oldLevelNumber,
                session.oldLevelExp,
                session.levelNumber,
                session.levelExp,
                profile.levelTitle,
                profile.plateEffectId,
                profile.plateId,
                session.vp,
                0,
                request.getCr_cid(),
                request.getCr_tv(),
                contestSpecifier,
                String.join(",", *contestRewardType),
                String.join(",", *contestRewardValue),
                String.join(",", *contestRewardString1),
                String.join(",", *contestRewardString2),
                contestEntryRewardType,
                contestEntryRewardValue,
                contestEntryRewardString1,
                contestEntryRewardString2,
                "xxx,xxx,xxx,xxx,xxx",
                "-1,-1,-1,-1,-1",
                "xxx,xxx,xxx,xxx,xxx",
                "xxx,xxx,xxx,xxx,xxx",
                "xxx,xxx,xxx,xxx,xxx",
                "xxx,xxx,xxx,xxx,xxx",
                "xxx,xxx,xxx,xxx,xxx",
                0,
                LocalDateTime.now(),
                -1,
                -1,
                0,
                0,
                0,
                -1,
                Const.NULL_QUEST,
                Const.NULL_QUEST,
                Const.NULL_QUEST,
                Const.NULL_QUEST,
                Const.NULL_QUEST,
                "-1,-1,-1,-1,-1",
                "-1,-1,-1,-1,-1",
                "-1,-1,-1,-1,-1"
            )
        } else {
            return StageResultResponse(
                request.cmd,
                request.req_id,
                "ok"
            )
        }
    }

    private fun getLog(request: StageResultRequest, profile: PlayerProfile?, i: Int): PlayLog {
        return PlayLog(
            profile,
            request.getStg_ply_pv_id()[i],
            Difficulty.fromValue(request.getStg_difficulty()[i]),
            Edition.fromValue(request.getStg_edtn()[i]),
            request.getStg_scrpt_ver()[i],
            request.getStg_score()[i],
            ChallengeKind.fromValue(request.getStg_chllng_kind()[i]),
            request.getStg_chllng_result()[i],
            ClearResult.fromValue(request.getStg_clr_kind()[i]),
            request.getStg_vcld_pts()[i],
            request.getStg_cool_cnt()[i],
            request.getStg_cool_pct()[i],
            request.getStg_fine_cnt()[i],
            request.getStg_fine_pct()[i],
            request.getStg_safe_cnt()[i],
            request.getStg_safe_pct()[i],
            request.getStg_sad_cnt()[i],
            request.getStg_sad_pct()[i],
            request.getStg_wt_wg_cnt()[i],
            request.getStg_wt_wg_pct()[i],
            request.getStg_max_cmb()[i],
            request.getStg_chance_tm()[i],
            request.getStg_sm_hl()[i],
            request.getStg_atn_pnt()[i],
            request.getStg_skin_id()[i],
            request.getStg_btn_se()[i],
            request.getStg_btn_se_vol()[i],
            request.getStg_sld_se()[i],
            request.getStg_chn_sld_se()[i],
            request.getStg_sldr_tch_se()[i],
            slice(request.getStg_mdl_id(), 3, i),
            request.getStg_cpt_rslt()[i],
            request.getStg_sld_scr()[i],
            request.getStg_vcl_chg()[i],
            slice(request.getStg_c_itm_id(), 12, i),
            slice(request.getStg_rgo(), 3, i),
            request.getStg_ss_num()[i],
            request.time_stamp.toLocalDateTime()
        )
    }

    fun slice(arr: IntArray, length: Int, offset: Int): kotlin.String {
        val sb = StringBuilder()

        for (i in length * offset..<length * (offset + 1)) {
            sb.append(arr[i]).append(",")
        }

        sb.deleteCharAt(sb.length - 1)
        return sb.toString()
    }

    private fun getContestProgress(arr: Array<kotlin.String?>): MutableList<ContestProgress> {
        val result: MutableList<ContestProgress> = LinkedList<ContestProgress>()
        var i = 0
        while (i < arr.size) {
            if (arr[i] != "-1") {
                result.add(
                    ContestProgress(
                        arr[i]!!.toInt(),
                        arr[i + 1]!!.toInt(),
                        arr[i + 2]!!.toInt(),
                        arr[i + 3]!!.toInt(),
                        arr[i + 4]!!.toInt(),
                        arr[i + 5]!!.toInt()
                    )
                )
            }
            i += 6
        }
        return result
    }

    private fun getContestSpecifier(progresses: MutableList<ContestProgress>): kotlin.String {
        val result: MutableList<kotlin.String?> = LinkedList<kotlin.String?>()
        for (x in progresses) {
            result.add(x.getHardness().toString())
            result.add(x.getEdition().toString())
            result.add(x.getStars().toString())
            result.add(x.getScores().toString())
            result.add(x.getVersion().toString())
        }
        while (result.size < 60) {
            result.add("-1")
        }
        return String.join(",", result)
    }

    private fun updateReward(
        currentValue: Int,
        previousValue: Int,
        borders: Int,
        reward: kotlin.String?
    ): MutableMap<kotlin.String?, kotlin.String?>? {
        if (currentValue > borders && previousValue < borders) {
            if (StringUtils.isNotBlank(reward)) {
                val rewardValue: Array<kotlin.String?> =
                    reward!!.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val result: MutableMap<kotlin.String?, kotlin.String?> = HashMap<kotlin.String?, kotlin.String?>()
                when (rewardValue[0]) {
                    "-1" -> return null
                    "0" -> {
                        result["type"] = rewardValue[0]
                        result["value"] = rewardValue[1]
                        result["string1"] = "***"
                        result["string2"] = "***"
                    }

                    "1" -> {
                        if (playerInventoryRepository.findByPdIdAndTypeAndValue(currentProfile!!, "SKIN", rewardValue[1]!!)
                                .isPresent
                        ) {
                            result["type"] = "-1"
                            result["value"] = "-1"
                            result["string1"] = "***"
                            result["string2"] = "***"
                        } else {
                            playerInventoryRepository.save<PlayerInventory?>(
                                PlayerInventory(
                                    null,
                                    currentProfile,
                                    rewardValue[1],
                                    "SKIN"
                                )
                            )
                            result.put("type", rewardValue[0])
                            result.put("value", rewardValue[1])
                            result.put("string1", rewardValue[2])
                            result.put("string2", rewardValue[3])
                        }
                    }

                    "2" -> {
                        if (playerInventoryRepository.findByPdIdAndTypeAndValue(currentProfile!!, "PLATE", rewardValue[1]!!)
                                .isPresent
                        ) {
                            result.put("type", "-1")
                            result.put("value", "-1")
                            result.put("string1", "***")
                            result.put("string2", "***")
                        } else {
                            playerInventoryRepository.save<PlayerInventory?>(
                                PlayerInventory(
                                    null,
                                    currentProfile,
                                    rewardValue[1],
                                    "PLATE"
                                )
                            )
                            result.put("type", rewardValue[0])
                            result.put("value", rewardValue[1])
                            result.put("string1", rewardValue[2])
                            result.put("string2", rewardValue[3])
                        }
                    }

                    "3" -> {
                        if (playerCustomizeRepository.findByPdIdAndCustomizeId(currentProfile!!, rewardValue[1]!!.toInt())
                                .isPresent
                        ) {
                            result.put("type", "-1")
                            result.put("value", "-1")
                            result.put("string1", "***")
                            result.put("string2", "***")
                        } else {
                            playerCustomizeRepository.save(
                                PlayerCustomize(
                                    currentProfile,
                                    rewardValue[1]!!.toInt()
                                )
                            )
                            result.put("type", rewardValue[0])
                            result.put("value", rewardValue[1])
                            result.put("string1", rewardValue[2])
                            result.put("string2", rewardValue[3])
                        }
                    }
                }
                return result
            }
        }
        return null
    }
}
