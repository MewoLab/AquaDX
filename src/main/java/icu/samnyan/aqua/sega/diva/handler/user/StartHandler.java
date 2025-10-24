package icu.samnyan.aqua.sega.diva.handler.user;

import icu.samnyan.aqua.sega.diva.dao.userdata.GameSessionRepository;
import icu.samnyan.aqua.sega.diva.dao.userdata.PlayerContestRepository;
import icu.samnyan.aqua.sega.diva.dao.userdata.PlayerPvRecordRepository;
import icu.samnyan.aqua.sega.diva.exception.ProfileNotFoundException;
import icu.samnyan.aqua.sega.diva.exception.PvRecordDataException;
import icu.samnyan.aqua.sega.diva.exception.SessionNotFoundException;
import icu.samnyan.aqua.sega.diva.handler.BaseHandler;
import icu.samnyan.aqua.sega.diva.model.common.Result;
import icu.samnyan.aqua.sega.diva.model.common.StartMode;
import icu.samnyan.aqua.sega.diva.model.common.collection.ClearSet;
import icu.samnyan.aqua.sega.diva.model.common.collection.ClearTally;
import icu.samnyan.aqua.sega.diva.model.request.user.StartRequest;
import icu.samnyan.aqua.sega.diva.model.response.user.StartResponse;
import icu.samnyan.aqua.sega.diva.model.userdata.GameSession;
import icu.samnyan.aqua.sega.diva.model.userdata.PlayerContest;
import icu.samnyan.aqua.sega.diva.model.userdata.PlayerProfile;
import icu.samnyan.aqua.sega.diva.model.userdata.PlayerPvRecord;
import icu.samnyan.aqua.sega.diva.service.PlayerCustomizeService;
import icu.samnyan.aqua.sega.diva.service.PlayerModuleService;
import icu.samnyan.aqua.sega.diva.service.PlayerProfileService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
@AllArgsConstructor
public class StartHandler extends BaseHandler {
    private static final Logger logger = LoggerFactory.getLogger(StartHandler.class);
    private final PlayerProfileService playerProfileService;
    private final GameSessionRepository gameSessionRepository;
    private final PlayerCustomizeService playerCustomizeService;
    private final PlayerModuleService playerModuleService;
    private final PlayerPvRecordRepository playerPvRecordRepository;
    private final PlayerContestRepository playerContestRepository;

    public String handle(StartRequest request) {

        PlayerProfile profile = playerProfileService.findByPdId(request.getPd_id()).orElseThrow(ProfileNotFoundException::new);
        GameSession session = gameSessionRepository.findByPdId(profile).orElseThrow(SessionNotFoundException::new);

        session.setStartMode(StartMode.START);
        gameSessionRepository.save(session);

        String module_have = playerModuleService.getModuleHaveString(profile);
        String customize_have = playerCustomizeService.getModuleHaveString(profile);

        Map<String, String> contestResult = getContestResult(profile);

        int border = profile.isShowGreatBorder() ? 1 : 0;
        border = border | ((profile.isShowExcellentBorder() ? 1 : 0) << 1);
        border = border | ((profile.isShowRivalBorder() ? 1 : 0) << 2);

        StartResponse response = new StartResponse(
                request.getCmd(),
                request.getReq_id(),
                "ok",
                profile.getPdId(),
                Result.SUCCESS,
                session.getAcceptId(),
                session.getAcceptId(),
                profile.getPlayerName(),
                profile.getHeadphoneVolume(),
                profile.isButtonSeOn(),
                profile.getButtonSeVolume(),
                profile.getSliderSeVolume(),
                profile.getSortMode(),
                profile.getLevel(),
                profile.getLevelExp(),
                profile.getLevelTitle(),
                profile.getPlateEffectId(),
                profile.getPlateId(),
                profile.getCommonModule(),
                profile.getCommonCustomizeItems(),
                profile.getModuleSelectItemFlag(),
                LocalDateTime.now(),
                module_have,
                customize_have,
                profile.isPreferPerPvModule(),
                profile.isPreferCommonModule(),
                profile.isUsePerPvSkin(),
                profile.isUsePerPvButtonSe(),
                profile.isUsePerPvSliderSe(),
                profile.isUsePerPvChainSliderSe(),
                profile.isUsePerPvTouchSliderSe(),
                profile.getVocaloidPoints(),
                profile.getNextPvId(),
                profile.getNextDifficulty(),
                profile.getNextEdition(),
                contestResult.get("cv_cid"), // contest progress
                contestResult.get("cv_sc"),
                contestResult.get("cv_rr"),
                contestResult.get("cv_bv"),
                contestResult.get("cv_bf"),
                profile.isContestNowPlayingEnable() ? profile.getContestNowPlayingId() : -1,
                profile.getContestNowPlayingValue(),
                profile.getContestNowPlayingResultRank(),
                profile.getContestNowPlayingSpecifier(),
                profile.getMyList0(),
                profile.getMyList1(),
                profile.getMyList2(),
                null,
                null,
//                getDummyString("-1", 40),
//                getDummyString("-1", 40),
                String.valueOf(border),
                profile.isShowInterimRanking(),
                profile.isShowClearStatus(),
                countClearStatus(profile),
                profile.isShowRgoSetting(),
                null, // Currently quest not working
                null,
                null,
                null,
                null,
                null
        );

        String resp = this.build(mapper.toMap(response));
        logger.info("Response: {}", resp);

        return resp;
    }

    private String countClearStatus(PlayerProfile profile) {
        List<PlayerPvRecord> pvRecordList = playerPvRecordRepository.findByPdId(profile);
        ClearTally clearTally = new ClearTally();
        pvRecordList.forEach(x -> {
            switch (x.getEdition()) {
                case ORIGINAL: {
                    switch (x.getResult()) {
                        case CHEAP:
                            getDiff(x, clearTally).addClear();
                            break;
                        case STANDARD:
                            getDiff(x, clearTally).addClear();
                            break;
                        case GREAT:
                            getDiff(x, clearTally).addGreat();
                            break;
                        case EXCELLENT:
                            getDiff(x, clearTally).addExcellent();
                            break;
                        case PERFECT:
                            getDiff(x, clearTally).addPerfect();
                            break;
                    }
                    break;
                }
                case EXTRA: {
                    switch (x.getResult()) {
                        case CHEAP:
                            clearTally.getExtraExtreme().addClear();
                            break;
                        case STANDARD:
                            clearTally.getExtraExtreme().addClear();
                            break;
                        case GREAT:
                            clearTally.getExtraExtreme().addGreat();
                            break;
                        case EXCELLENT:
                            clearTally.getExtraExtreme().addExcellent();
                            break;
                        case PERFECT:
                            clearTally.getExtraExtreme().addPerfect();
                            break;
                    }
                }
            }
        });
        return clearTally.toInternal();
    }

    private ClearSet getDiff(PlayerPvRecord record, ClearTally clearTally) {
        switch (record.getDifficulty()) {
            case EASY:
                return clearTally.getEasy();
            case NORMAL:
                return clearTally.getNormal();
            case HARD:
                return clearTally.getHard();
            case EXTREME:
                return clearTally.getExtreme();
            default:
                throw new PvRecordDataException("Difficulty data not exist, record id:" + record.getId());
        }
    }

    private Map<String, String> getContestResult(PlayerProfile profile) {
        List<Integer> cv_cid = new LinkedList<>();
        List<Integer> cv_sc = new LinkedList<>();
        List<Integer> cv_rr = new LinkedList<>();
        List<Integer> cv_bv = new LinkedList<>();
        List<Integer> cv_bf = new LinkedList<>();
        List<PlayerContest> contestList = playerContestRepository.findTop4ByPdIdOrderByLastUpdateTimeDesc(profile);
        contestList.forEach(x -> {
            cv_cid.add(x.getContestId());
            cv_sc.add(x.getStartCount());
            cv_rr.add(x.getResultRank().getValue());
            cv_bv.add(x.getBestValue());
            cv_bf.add(-1);
        });
        for (int i = cv_cid.size(); i < 4; i++) {
            cv_cid.add(-1);
            cv_sc.add(-1);
            cv_rr.add(-1);
            cv_bv.add(-1);
            cv_bf.add(-1);
        }
        Map<String, String> result = new HashMap<>();
        result.put("cv_cid", cv_cid.stream().map(Object::toString).collect(Collectors.joining(",")));
        result.put("cv_sc", cv_sc.stream().map(Object::toString).collect(Collectors.joining(",")));
        result.put("cv_rr", cv_rr.stream().map(Object::toString).collect(Collectors.joining(",")));
        result.put("cv_bv", cv_bv.stream().map(Object::toString).collect(Collectors.joining(",")));
        result.put("cv_bf", cv_bf.stream().map(Object::toString).collect(Collectors.joining(",")));
        return result;
    }
}
