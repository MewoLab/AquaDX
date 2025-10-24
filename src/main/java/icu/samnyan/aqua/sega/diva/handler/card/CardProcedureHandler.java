package icu.samnyan.aqua.sega.diva.handler.card;

import icu.samnyan.aqua.sega.diva.dao.userdata.GameSessionRepository;
import icu.samnyan.aqua.sega.diva.handler.BaseHandler;
import icu.samnyan.aqua.sega.diva.model.common.Result;
import icu.samnyan.aqua.sega.diva.model.common.StartMode;
import icu.samnyan.aqua.sega.diva.model.request.card.CardProcedureRequest;
import icu.samnyan.aqua.sega.diva.model.response.card.CardProcedureResponse;
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
public class CardProcedureHandler extends BaseHandler {
    private static final Logger logger = LoggerFactory.getLogger(CardProcedureHandler.class);

    private final PlayerProfileService playerProfileService;

    private final GameSessionRepository gameSessionRepository;

    public String handle(CardProcedureRequest request) {
        Optional<PlayerProfile> profileOptional = playerProfileService.findByPdId(request.getAime_id());
        CardProcedureResponse response;
        if (profileOptional.isEmpty()) {
            response = new CardProcedureResponse(
                    request.getCmd(),
                    request.getReq_id(),
                    "ok",
                    Result.FAILED
            );
        } else {
            PlayerProfile profile = profileOptional.get();

            Optional<GameSession> sessionOptional = gameSessionRepository.findByPdId(profile);
            if (sessionOptional.isPresent()) {
                GameSession session = sessionOptional.get();
                if (session.getLastUpdateTime().isBefore(LocalDateTime.now().minusMinutes(5))) {
                    gameSessionRepository.delete(session);
                }
                response = new CardProcedureResponse(
                        request.getCmd(),
                        request.getReq_id(),
                        "ok",
                        Result.FAILED
                );
            } else {
                GameSession session = new GameSession(
                        ThreadLocalRandom.current().nextInt(100, 99999),
                        profile,
                        StartMode.CARD_PROCEDURE,
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
                response = new CardProcedureResponse(
                        request.getCmd(),
                        request.getReq_id(),
                        "ok",
                        Result.SUCCESS,
                        100,
                        session.getAcceptId(),
                        profile.getPdId(),
                        profile.getPlayerName(),
                        profile.getLevel(),
                        profile.getLevelExp(),
                        profile.getLevelTitle(),
                        profile.getPlateEffectId(),
                        profile.getPlateId(),
                        profile.getVocaloidPoints(),
                        profile.getPasswordStatus()
                );
            }
        }
        String resp = this.build(mapper.toMap(response));
        logger.info("Response: {}", resp);

        return resp;
    }
}
