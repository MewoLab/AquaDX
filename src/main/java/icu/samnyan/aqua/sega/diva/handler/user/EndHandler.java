package icu.samnyan.aqua.sega.diva.handler.user;

import icu.samnyan.aqua.sega.diva.dao.gamedata.ContestRepository;
import icu.samnyan.aqua.sega.diva.dao.userdata.GameSessionRepository;
import icu.samnyan.aqua.sega.diva.dao.userdata.PlayerContestRepository;
import icu.samnyan.aqua.sega.diva.exception.ProfileNotFoundException;
import icu.samnyan.aqua.sega.diva.exception.SessionNotFoundException;
import icu.samnyan.aqua.sega.diva.handler.BaseHandler;
import icu.samnyan.aqua.sega.diva.model.common.ContestBorder;
import icu.samnyan.aqua.sega.diva.model.common.Difficulty;
import icu.samnyan.aqua.sega.diva.model.common.Edition;
import icu.samnyan.aqua.sega.diva.model.common.SortMode;
import icu.samnyan.aqua.sega.diva.model.gamedata.Contest;
import icu.samnyan.aqua.sega.diva.model.request.ingame.StageResultRequest;
import icu.samnyan.aqua.sega.diva.model.response.BaseResponse;
import icu.samnyan.aqua.sega.diva.model.userdata.GameSession;
import icu.samnyan.aqua.sega.diva.model.userdata.PlayerContest;
import icu.samnyan.aqua.sega.diva.model.userdata.PlayerProfile;
import icu.samnyan.aqua.sega.diva.service.PlayerProfileService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static icu.samnyan.aqua.sega.diva.util.DivaStringUtils.getDummyString;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
@AllArgsConstructor
public class EndHandler extends BaseHandler {
    private static final Logger logger = LoggerFactory.getLogger(EndHandler.class);

    private final ContestRepository contestRepository;

    private final PlayerProfileService playerProfileService;

    private final PlayerContestRepository playerContestRepository;

    private final GameSessionRepository gameSessionRepository;

    public String handle(StageResultRequest request) {

        PlayerProfile profile = playerProfileService.findByPdId(request.getPd_id()).orElseThrow(ProfileNotFoundException::new);
        GameSession session = gameSessionRepository.findByPdId(profile).orElseThrow(SessionNotFoundException::new);


        profile.setHeadphoneVolume(request.getHp_vol());
        profile.setButtonSeOn(request.isBtn_se_vol());
        profile.setButtonSeVolume(request.getBtn_se_vol2());
        profile.setSliderSeVolume(request.getSldr_se_vol2());
        profile.setVocaloidPoints(session.getVp());
        profile.setLevel(session.getLevelNumber());
        profile.setLevelExp(session.getLevelExp());
        profile.setNextPvId(request.getNxt_pv_id());
        profile.setNextDifficulty(Difficulty.fromValue(request.getNxt_dffclty()));
        profile.setNextEdition(Edition.fromValue(request.getNxt_edtn()));
        profile.setSortMode(SortMode.fromValue(request.getSort_kind()));

        if (request.getCr_cid() != -1) {
            Contest contest = contestRepository.findById(request.getCr_cid()).orElseGet(Contest::new);
            ContestBorder currentResultRank = getContestRank(contest, request.getCr_tv());
            if (request.getCr_if() == 0) {
                // Do contest is playing
                profile.setContestNowPlayingEnable(true);
                profile.setContestNowPlayingId(request.getCr_cid());
                profile.setContestNowPlayingResultRank(currentResultRank);
                profile.setContestNowPlayingValue(request.getCr_tv());
                profile.setContestNowPlayingSpecifier(String.join(",", request.getCr_sp()));
            } else {
                PlayerContest contestRecord = playerContestRepository.findByPdIdAndContestId(profile, request.getCr_cid()).orElseGet(() -> new PlayerContest(profile, request.getCr_cid()));
                contestRecord.setStartCount(contestRecord.getStartCount() + 1);
                contestRecord.setBestValue(Math.max(contestRecord.getBestValue(), request.getCr_tv()));
                contestRecord.setResultRank(currentResultRank.getValue() > contestRecord.getResultRank().getValue() ? currentResultRank : contestRecord.getResultRank());
                contestRecord.setLastUpdateTime(LocalDateTime.now());

                playerContestRepository.save(contestRecord);
                profile.setContestNowPlayingEnable(false);
                profile.setContestNowPlayingId(-1);
                profile.setContestNowPlayingResultRank(ContestBorder.NONE);
                profile.setContestNowPlayingValue(-1);
                profile.setContestNowPlayingSpecifier(getDummyString("-1", 60));
            }
        }
        
        playerProfileService.save(profile);
        gameSessionRepository.delete(session);


        BaseResponse response = new BaseResponse(
                request.getCmd(),
                request.getReq_id(),
                "ok"
        );

        String resp = this.build(mapper.toMap(response));
        logger.info("Response: {}", resp);

        return resp;
    }

    private ContestBorder getContestRank(Contest contest, int value) {
        if (value >= contest.getGoldBorders()) return ContestBorder.GOLD;
        if (value >= contest.getSliverBorders()) return ContestBorder.SILVER;
        if (value >= contest.getBronzeBorders()) return ContestBorder.BRONZE;
        return ContestBorder.NONE;
    }
}
