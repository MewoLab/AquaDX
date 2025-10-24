package icu.samnyan.aqua.sega.diva.handler.card;

import icu.samnyan.aqua.sega.diva.dao.userdata.GameSessionRepository;
import icu.samnyan.aqua.sega.diva.exception.ProfileNotFoundException;
import icu.samnyan.aqua.sega.diva.exception.SessionNotFoundException;
import icu.samnyan.aqua.sega.diva.handler.BaseHandler;
import icu.samnyan.aqua.sega.diva.model.common.Result;
import icu.samnyan.aqua.sega.diva.model.request.card.ChangeNameRequest;
import icu.samnyan.aqua.sega.diva.model.response.card.ChangeNameResponse;
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
public class ChangeNameHandler extends BaseHandler {
    private static final Logger logger = LoggerFactory.getLogger(ChangeNameHandler.class);

    private final PlayerProfileService playerProfileService;

    private final GameSessionRepository gameSessionRepository;

    public String handle(ChangeNameRequest request) {
        PlayerProfile profile = playerProfileService.findByPdId(request.getPd_id()).orElseThrow(ProfileNotFoundException::new);
        GameSession session = gameSessionRepository.findByPdId(profile).orElseThrow(SessionNotFoundException::new);

        profile.setPlayerName(request.getPlayer_name());

        ChangeNameResponse response = new ChangeNameResponse(
                request.getCmd(),
                request.getReq_id(),
                "ok",
                Result.SUCCESS,
                session.getAcceptId(),
                profile.getPdId(),
                profile.getPlayerName()
        );

        playerProfileService.save(profile);
        gameSessionRepository.delete(session);

        String resp = this.build(mapper.toMap(response));
        logger.info("Response: {}", resp);

        return resp;
    }
}
