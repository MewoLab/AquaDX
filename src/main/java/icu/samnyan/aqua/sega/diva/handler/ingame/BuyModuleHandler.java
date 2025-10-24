package icu.samnyan.aqua.sega.diva.handler.ingame;

import icu.samnyan.aqua.sega.diva.dao.gamedata.DivaModuleRepository;
import icu.samnyan.aqua.sega.diva.dao.userdata.GameSessionRepository;
import icu.samnyan.aqua.sega.diva.exception.ProfileNotFoundException;
import icu.samnyan.aqua.sega.diva.exception.SessionNotFoundException;
import icu.samnyan.aqua.sega.diva.handler.BaseHandler;
import icu.samnyan.aqua.sega.diva.model.common.Result;
import icu.samnyan.aqua.sega.diva.model.gamedata.DivaModule;
import icu.samnyan.aqua.sega.diva.model.request.ingame.BuyModuleRequest;
import icu.samnyan.aqua.sega.diva.model.response.ingame.BuyModuleResponse;
import icu.samnyan.aqua.sega.diva.model.userdata.GameSession;
import icu.samnyan.aqua.sega.diva.model.userdata.PlayerProfile;
import icu.samnyan.aqua.sega.diva.service.PlayerModuleService;
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
public class BuyModuleHandler extends BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(BuyModuleHandler.class);

    private final DivaModuleRepository divaModuleRepository;

    private final PlayerProfileService playerProfileService;

    private final PlayerModuleService playerModuleService;

    private final GameSessionRepository gameSessionRepository;

    public String handle(BuyModuleRequest request) {
        PlayerProfile profile = playerProfileService.findByPdId(request.getPd_id()).orElseThrow(ProfileNotFoundException::new);

        GameSession session = gameSessionRepository.findByPdId(profile).orElseThrow(SessionNotFoundException::new);

        Optional<DivaModule> moduleOptional = divaModuleRepository.findById(request.getMdl_id());

        BuyModuleResponse response;
        if (moduleOptional.isEmpty()) {
            response = new BuyModuleResponse(
                    request.getCmd(),
                    request.getReq_id(),
                    "ok",
                    Result.FAILED
            );
        } else {
            if (session.getVp() < moduleOptional.get().getPrice()) {
                response = new BuyModuleResponse(
                        request.getCmd(),
                        request.getReq_id(),
                        "ok",
                        Result.FAILED
                );
            } else {
                playerModuleService.buy(profile, request.getMdl_id());
                session.setVp(session.getVp() - moduleOptional.get().getPrice());
                gameSessionRepository.save(session);

                response = new BuyModuleResponse(
                        request.getCmd(),
                        request.getReq_id(),
                        "ok",
                        Result.SUCCESS,
                        request.getMdl_id(),
                        playerModuleService.getModuleHaveString(profile),
                        session.getVp()
                );
            }
        }

        String resp = this.build(mapper.toMap(response));
        logger.info("Response: {}", resp);

        return resp;
    }
}
