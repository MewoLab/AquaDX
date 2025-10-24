package icu.samnyan.aqua.sega.diva.handler.user;

import icu.samnyan.aqua.sega.diva.dao.userdata.GameSessionRepository;
import icu.samnyan.aqua.sega.diva.exception.ProfileNotFoundException;
import icu.samnyan.aqua.sega.diva.exception.SessionNotFoundException;
import icu.samnyan.aqua.sega.diva.handler.BaseHandler;
import icu.samnyan.aqua.sega.diva.model.request.user.PdUnlockRequest;
import icu.samnyan.aqua.sega.diva.model.response.BaseResponse;
import icu.samnyan.aqua.sega.diva.model.userdata.GameSession;
import icu.samnyan.aqua.sega.diva.model.userdata.PlayerProfile;
import icu.samnyan.aqua.sega.diva.service.PlayerProfileService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
@AllArgsConstructor
public class PdUnlockHandler extends BaseHandler {
    private static final Logger logger = LoggerFactory.getLogger(PdUnlockHandler.class);
    private final PlayerProfileService playerProfileService;
    private final GameSessionRepository gameSessionRepository;

    public String handle(PdUnlockRequest request) {

        PlayerProfile profile = playerProfileService.findByPdId(request.getPd_id()).orElseThrow(ProfileNotFoundException::new);
        GameSession session = gameSessionRepository.findByPdId(profile).orElseThrow(SessionNotFoundException::new);

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
}
