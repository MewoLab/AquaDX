package icu.samnyan.aqua.sega.diva.handler.ingame;

import icu.samnyan.aqua.sega.diva.dao.gamedata.DivaCustomizeRepository;
import icu.samnyan.aqua.sega.diva.dao.userdata.GameSessionRepository;
import icu.samnyan.aqua.sega.diva.exception.ProfileNotFoundException;
import icu.samnyan.aqua.sega.diva.exception.SessionNotFoundException;
import icu.samnyan.aqua.sega.diva.handler.BaseHandler;
import icu.samnyan.aqua.sega.diva.model.common.Result;
import icu.samnyan.aqua.sega.diva.model.gamedata.DivaCustomize;
import icu.samnyan.aqua.sega.diva.model.request.ingame.BuyCstmzItmRequest;
import icu.samnyan.aqua.sega.diva.model.response.ingame.BuyCstmzItmResponse;
import icu.samnyan.aqua.sega.diva.model.userdata.GameSession;
import icu.samnyan.aqua.sega.diva.model.userdata.PlayerProfile;
import icu.samnyan.aqua.sega.diva.service.PlayerCustomizeService;
import icu.samnyan.aqua.sega.diva.service.PlayerProfileService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
@AllArgsConstructor
public class BuyCstmzItmHandler extends BaseHandler {
    private static final Logger logger = LoggerFactory.getLogger(BuyCstmzItmHandler.class);

    private final DivaCustomizeRepository divaCustomizeRepository;

    private final PlayerProfileService playerProfileService;

    private final PlayerCustomizeService playerCustomizeService;

    private final GameSessionRepository gameSessionRepository;

    public String handle(BuyCstmzItmRequest request) {
        PlayerProfile profile = playerProfileService.findByPdId(request.getPd_id()).orElseThrow(ProfileNotFoundException::new);

        GameSession session = gameSessionRepository.findByPdId(profile).orElseThrow(SessionNotFoundException::new);

        Optional<DivaCustomize> customizeOptional = divaCustomizeRepository.findById(request.getCstmz_itm_id());

        BuyCstmzItmResponse response;
        if (customizeOptional.isEmpty()) {
            response = new BuyCstmzItmResponse(
                    request.getCmd(),
                    request.getReq_id(),
                    "ok",
                    Result.FAILED
            );
        } else {
            if (session.getVp() < customizeOptional.get().getPrice()) {
                response = new BuyCstmzItmResponse(
                        request.getCmd(),
                        request.getReq_id(),
                        "ok",
                        Result.FAILED
                );
            } else {
                playerCustomizeService.buy(profile, request.getCstmz_itm_id());
                session.setVp(session.getVp() - customizeOptional.get().getPrice());
                gameSessionRepository.save(session);

                response = new BuyCstmzItmResponse(
                        request.getCmd(),
                        request.getReq_id(),
                        "ok",
                        Result.SUCCESS,
                        request.getCstmz_itm_id(),
                        playerCustomizeService.getModuleHaveString(profile),
                        session.getVp()
                );
            }
        }

        String resp = this.build(mapper.toMap(response));
        logger.info("Response: {}", resp);

        return resp;
    }
}
