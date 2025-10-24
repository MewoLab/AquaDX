package icu.samnyan.aqua.sega.diva.handler.ingame;

import icu.samnyan.aqua.sega.diva.dao.gamedata.ContestRepository;
import icu.samnyan.aqua.sega.diva.dao.userdata.*;
import icu.samnyan.aqua.sega.diva.exception.ProfileNotFoundException;
import icu.samnyan.aqua.sega.diva.exception.SessionNotFoundException;
import icu.samnyan.aqua.sega.diva.handler.BaseHandler;
import icu.samnyan.aqua.sega.diva.model.common.*;
import icu.samnyan.aqua.sega.diva.model.gamedata.Contest;
import icu.samnyan.aqua.sega.diva.model.request.ingame.StageResultRequest;
import icu.samnyan.aqua.sega.diva.model.response.ingame.StageResultResponse;
import icu.samnyan.aqua.sega.diva.model.userdata.*;
import icu.samnyan.aqua.sega.diva.service.PlayerProfileService;
import icu.samnyan.aqua.sega.diva.util.DivaCalculator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

import static icu.samnyan.aqua.sega.diva.model.common.Const.NULL_QUEST;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
@RequiredArgsConstructor
public class StageResultHandler extends BaseHandler {
    private static final Logger logger = LoggerFactory.getLogger(StageResultHandler.class);
    private final GameSessionRepository gameSessionRepository;
    private final PlayerPvRecordRepository pvRecordRepository;
    private final PlayerProfileService playerProfileService;
    private final PlayLogRepository playLogRepository;
    private final ContestRepository contestRepository;
    private final PlayerContestRepository playerContestRepository;
    private final PlayerCustomizeRepository playerCustomizeRepository;
    private final PlayerInventoryRepository playerInventoryRepository;

    private final DivaCalculator divaCalculator;

    private PlayerProfile currentProfile = null;

    public String handle(StageResultRequest request) {
        StageResultResponse response;
        if (request.getPd_id() != -1) {
            PlayerProfile profile = playerProfileService.findByPdId(request.getPd_id()).orElseThrow(ProfileNotFoundException::new);
            GameSession session = gameSessionRepository.findByPdId(profile).orElseThrow(SessionNotFoundException::new);

            currentProfile = profile;
            // Get the last played index
            int[] pvIds = request.getStg_ply_pv_id();
            int[] stageArr = request.getStg_ply_pv_id();
            int stageIndex = 0;
            if (stageArr[0] != -1) {
                stageIndex = 0;
            }
            if (stageArr[1] != -1) {
                stageIndex = 1;
            }
            if (stageArr[2] != -1) {
                stageIndex = 2;
            }
            if (stageArr[3] != -1) {
                stageIndex = 3;
            }

            // Convert to play log object
            PlayLog log = getLog(request, profile, stageIndex);
            logger.debug("Stage Result Object: {}", log.toString());

            PlayerPvRecord record = pvRecordRepository.findByPdIdAndPvIdAndEditionAndDifficulty(profile, log.getPvId(), log.getEdition(), log.getDifficulty())
                    .orElseGet(() -> new PlayerPvRecord(profile, log.getPvId(), log.getEdition(), log.getDifficulty()));

            // Not save personal record in no fail mode
            if (request.getGame_type() != 1) {
                // Only update personal record when using rhythm game option
                if (log.getRhythmGameOptions().equals("0,0,0")) {
                    // Update pvRecord field
                    record.setMaxScore(Math.max(record.getMaxScore(), log.getScore()));
                    record.setMaxAttain(Math.max(record.getMaxAttain(), log.getAttainPoint()));

                    if (record.getResult().getValue() < log.getClearResult().getValue()) {
                        record.setResult(log.getClearResult());
                    }
                }
            }

            String[] updateRgo = log.getRhythmGameOptions().split(",");
            String[] oldRgo = record.getRgoPlayed().split(",");
            for (int i = 0; i < updateRgo.length; i++) {
                if (updateRgo[i].equals("1")) {
                    oldRgo[i] = "1";
                }
            }
            record.setRgoPlayed(StringUtils.join(oldRgo, ","));

            session.setVp(session.getVp() + log.getVp());
            session.setLastPvId(log.getPvId());
            session.setLastUpdateTime(LocalDateTime.now());

            LevelInfo levelInfo = divaCalculator.getLevelInfo(profile);
            session.setOldLevelNumber(session.getLevelNumber());
            session.setOldLevelExp(session.getLevelExp());
            session.setLevelNumber(levelInfo.getLevelNumber());
            session.setLevelExp(levelInfo.getLevelExp());

            session.setStageResultIndex(stageIndex);

            // Calculate reward
            // Contest reward
            String contestSpecifier = String.join(",", request.getCr_sp());
            String[] contestRewardType = {"-1", "-1", "-1"};
            String[] contestRewardValue = {"-1", "-1", "-1"};
            String[] contestRewardString1 = {"***", "***", "***"};
            String[] contestRewardString2 = {"***", "***", "***"};
            int contestEntryRewardType = -1;
            int contestEntryRewardValue = -1;
            String contestEntryRewardString1 = "***";
            String contestEntryRewardString2 = "***";
            int contestId = request.getCr_cid();
            if (contestId != -1) {
                List<ContestProgress> progress = getContestProgress(request.getCr_sp());
                contestSpecifier = getContestSpecifier(progress);

                // Check if the contest info exist
                Optional<Contest> contestOptional = contestRepository.findById(contestId);
                if (contestOptional.isPresent()) {
                    Contest contest = contestOptional.get();
                    Optional<PlayerContest> playerContestOptional = playerContestRepository.findByPdIdAndContestId(profile, contestId);

                    // Contest Entry Reward
                    // Check if this is first stage
                    if (progress.size() == 1 && playerContestOptional.isEmpty()) {
                        if (StringUtils.isNotBlank(contest.getContestEntryReward())) {
                            // Check if this is first time play this contest
                            String reward = contest.getContestEntryReward();
                            String[] rewardValue = reward.split(":");

                            contestEntryRewardType = Integer.parseInt(rewardValue[0]);
                            contestEntryRewardValue = Integer.parseInt(rewardValue[1]);
                            contestEntryRewardString1 = rewardValue[2];
                            contestEntryRewardString2 = rewardValue[3];
                        }
                    }

                    // Only if this is the first time reach this value
                    int previousValue = progress.stream().limit(progress.size() - 1).mapToInt(ContestProgress::getScores).sum();
                    int currentValue = progress.stream().mapToInt(ContestProgress::getScores).sum();

                    // Bronze Reward
                    Map<String, String> bronze = updateReward(currentValue, previousValue, contest.getBronzeBorders(), contest.getBronzeContestReward());
                    if (bronze != null) {
                        contestRewardType[0] = bronze.get("type");
                        contestRewardValue[0] = bronze.get("value");
                        contestRewardString1[0] = bronze.get("string1");
                        contestRewardString2[2] = bronze.get("string2");
                    }

                    // Silver Reward
                    Map<String, String> silver = updateReward(currentValue, previousValue, contest.getSliverBorders(), contest.getSliverContestReward());
                    if (silver != null) {
                        contestRewardType[1] = silver.get("type");
                        contestRewardValue[1] = silver.get("value");
                        contestRewardString1[1] = silver.get("string1");
                        contestRewardString2[2] = silver.get("string2");
                    }

                    // Gold Reward
                    Map<String, String> gold = updateReward(currentValue, previousValue, contest.getGoldBorders(), contest.getGoldContestReward());
                    if (gold != null) {
                        contestRewardType[2] = gold.get("type");
                        contestRewardValue[2] = gold.get("value");
                        contestRewardString1[2] = gold.get("string1");
                        contestRewardString2[2] = gold.get("string2");
                    }

                }
            }

            pvRecordRepository.save(record);
            playLogRepository.save(log);
            gameSessionRepository.save(session);


            response = new StageResultResponse(
                    request.getCmd(),
                    request.getReq_id(),
                    "ok",
                    ChallengeKind.UNDEFINED.getValue(),
                    session.getOldLevelNumber(),
                    session.getOldLevelExp(),
                    session.getLevelNumber(),
                    session.getLevelExp(),
                    profile.getLevelTitle(),
                    profile.getPlateEffectId(),
                    profile.getPlateId(),
                    session.getVp(),
                    0,
                    request.getCr_cid(),
                    request.getCr_tv(),
                    contestSpecifier,
                    String.join(",", contestRewardType),
                    String.join(",", contestRewardValue),
                    String.join(",", contestRewardString1),
                    String.join(",", contestRewardString2),
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
                    NULL_QUEST,
                    NULL_QUEST,
                    NULL_QUEST,
                    NULL_QUEST,
                    NULL_QUEST,
                    "-1,-1,-1,-1,-1",
                    "-1,-1,-1,-1,-1",
                    "-1,-1,-1,-1,-1"
            );
        } else {
            response = new StageResultResponse(
                    request.getCmd(),
                    request.getReq_id(),
                    "ok"
            );
        }

        String resp = this.build(mapper.toMap(response));
        logger.info("Response: {}", resp);

        return resp;
    }

    private PlayLog getLog(StageResultRequest request, PlayerProfile profile, int i) {
        return new PlayLog(
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
                request.getTime_stamp().toLocalDateTime()
        );
    }

    public String slice(int[] arr, int length, int offset) {
        StringBuilder sb = new StringBuilder();

        for (int i = length * offset; i < length * (offset + 1); i++) {
            sb.append(arr[i]).append(",");
        }

        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    private List<ContestProgress> getContestProgress(String[] arr) {
        List<ContestProgress> result = new LinkedList<>();
        for (int i = 0; i < arr.length; i = i + 6) {
            if (!arr[i].equals("-1")) {
                result.add(new ContestProgress(
                        Integer.parseInt(arr[i]),
                        Integer.parseInt(arr[i + 1]),
                        Integer.parseInt(arr[i + 2]),
                        Integer.parseInt(arr[i + 3]),
                        Integer.parseInt(arr[i + 4]),
                        Integer.parseInt(arr[i + 5])
                ));
            }
        }
        return result;
    }

    private String getContestSpecifier(List<ContestProgress> progresses) {
        List<String> result = new LinkedList<>();
        for (ContestProgress x : progresses) {
            result.add(String.valueOf(x.getHardness()));
            result.add(String.valueOf(x.getEdition()));
            result.add(String.valueOf(x.getStars()));
            result.add(String.valueOf(x.getScores()));
            result.add(String.valueOf(x.getVersion()));
        }
        while (result.size() < 60) {
            result.add("-1");
        }
        return String.join(",", result);
    }

    private Map<String, String> updateReward(int currentValue, int previousValue, int borders, String reward) {
        if (currentValue > borders && previousValue < borders) {
            if (StringUtils.isNotBlank(reward)) {
                String[] rewardValue = reward.split(":");
                Map<String, String> result = new HashMap<>();
                switch (rewardValue[0]) {
                    case "-1":
                        return null;
                    case "0": {
                        result.put("type", rewardValue[0]);
                        result.put("value", rewardValue[1]);
                        result.put("string1", "***");
                        result.put("string2", "***");
                        break;
                    }
                    case "1": {
                        if (playerInventoryRepository.findByPdIdAndTypeAndValue(currentProfile, "SKIN", rewardValue[1]).isPresent()) {
                            result.put("type", "-1");
                            result.put("value", "-1");
                            result.put("string1", "***");
                            result.put("string2", "***");
                        } else {
                            playerInventoryRepository.save(new PlayerInventory(null, currentProfile, rewardValue[1], "SKIN"));
                            result.put("type", rewardValue[0]);
                            result.put("value", rewardValue[1]);
                            result.put("string1", rewardValue[2]);
                            result.put("string2", rewardValue[3]);
                        }
                        break;
                    }
                    case "2": {
                        if (playerInventoryRepository.findByPdIdAndTypeAndValue(currentProfile, "PLATE", rewardValue[1]).isPresent()) {
                            result.put("type", "-1");
                            result.put("value", "-1");
                            result.put("string1", "***");
                            result.put("string2", "***");
                        } else {
                            playerInventoryRepository.save(new PlayerInventory(null, currentProfile, rewardValue[1], "PLATE"));
                            result.put("type", rewardValue[0]);
                            result.put("value", rewardValue[1]);
                            result.put("string1", rewardValue[2]);
                            result.put("string2", rewardValue[3]);
                        }
                        break;
                    }
                    case "3": {
                        if (playerCustomizeRepository.findByPdIdAndCustomizeId(currentProfile, Integer.parseInt(rewardValue[1])).isPresent()) {
                            result.put("type", "-1");
                            result.put("value", "-1");
                            result.put("string1", "***");
                            result.put("string2", "***");
                        } else {
                            playerCustomizeRepository.save(new PlayerCustomize(currentProfile, Integer.parseInt(rewardValue[1])));
                            result.put("type", rewardValue[0]);
                            result.put("value", rewardValue[1]);
                            result.put("string1", rewardValue[2]);
                            result.put("string2", rewardValue[3]);
                        }
                        break;
                    }
                }
                return result;
            }
        }
        return null;
    }
}
