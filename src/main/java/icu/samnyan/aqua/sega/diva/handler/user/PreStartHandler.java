package icu.samnyan.aqua.sega.diva.handler.user;

import icu.samnyan.aqua.sega.diva.dao.userdata.GameSessionRepository;
import icu.samnyan.aqua.sega.diva.handler.BaseHandler;
import icu.samnyan.aqua.sega.diva.model.common.PreStartResult;
import icu.samnyan.aqua.sega.diva.model.common.StartMode;
import icu.samnyan.aqua.sega.diva.model.request.user.PreStartRequest;
import icu.samnyan.aqua.sega.diva.model.response.user.PreStartResponse;
import icu.samnyan.aqua.sega.diva.model.userdata.GameSession;
import icu.samnyan.aqua.sega.diva.model.userdata.PlayerProfile;
import icu.samnyan.aqua.sega.diva.service.PlayerProfileService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
@AllArgsConstructor
public class PreStartHandler extends BaseHandler {
    private static final Logger logger = LoggerFactory.getLogger(PreStartHandler.class);
    private final PlayerProfileService playerProfileService;
    private final GameSessionRepository gameSessionRepository;

    public String handle(PreStartRequest request) {
        Optional<PlayerProfile> profileOptional = playerProfileService.findByPdId(request.getAime_id());
        PreStartResponse response;
        boolean normalStart = false;
        if (profileOptional.isEmpty()) {
            response = new PreStartResponse(
                    request.getCmd(),
                    request.getReq_id(),
                    "ok",
                    PreStartResult.NEW_REGISTRATION);

            String resp = this.build(mapper.toMap(response));
            logger.info("Response: {}", resp);

            return resp;
        } else {
            PlayerProfile profile = profileOptional.get();

            Optional<GameSession> sessionOptional = gameSessionRepository.findByPdId(profile);
            if (sessionOptional.isPresent()) {
                GameSession session = sessionOptional.get();
                if (!session.getLastUpdateTime().isBefore(LocalDateTime.now().minusMinutes(5)) && session.getStartMode() == StartMode.START) {

                    response = new PreStartResponse(
                            request.getCmd(),
                            request.getReq_id(),
                            "ok",
                            PreStartResult.ALREADY_PLAYING
                    );

                    String resp = this.build(mapper.toMap(response));
                    logger.info("Response: {}", resp);

                    return resp;
                } else {
                    gameSessionRepository.delete(session);
                }
            }

            GameSession session = new GameSession(
                    ThreadLocalRandom.current().nextInt(100, 99999),
                    profile,
                    StartMode.PRE_START,
                    LocalDateTime.now(),
                    LocalDateTime.now(),
                    -1,
                    -1,
                    -1,
                    profile.getLevel(),
                    profile.getLevelExp(),
                    profile.getLevel(),
                    profile.getLevelExp(),
                    profile.getVocaloidPoints()
            );

            gameSessionRepository.save(session);

            response = new PreStartResponse(
                    request.getCmd(),
                    request.getReq_id(),
                    "ok",
                    PreStartResult.SUCCESS,
                    session.getAcceptId(),
                    profile.getPdId(),
                    profile.getPlayerName(),
                    profile.getSortMode(),
                    profile.getLevel(),
                    profile.getLevelExp(),
                    profile.getLevelTitle(),
                    profile.getPlateEffectId(),
                    profile.getPlateId(),
                    profile.getCommonModule(),
                    profile.getCommonModuleSetTime(),
                    profile.getCommonSkin(),
                    profile.getButtonSe(),
                    profile.getSlideSe(),
                    profile.getChainSlideSe(),
                    profile.getSliderTouchSe(),
                    profile.getVocaloidPoints(),
                    profile.getPasswordStatus()
            );

            String resp = this.build(mapper.toMap(response));
            logger.info("Response: {}", resp);

            return resp;
        }
    }
}
