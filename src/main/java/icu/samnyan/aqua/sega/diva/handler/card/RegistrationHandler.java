package icu.samnyan.aqua.sega.diva.handler.card;

import icu.samnyan.aqua.sega.diva.handler.BaseHandler;
import icu.samnyan.aqua.sega.diva.model.common.Result;
import icu.samnyan.aqua.sega.diva.model.request.card.RegistrationRequest;
import icu.samnyan.aqua.sega.diva.model.response.card.RegistrationResponse;
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
public class RegistrationHandler extends BaseHandler {
    private static final Logger logger = LoggerFactory.getLogger(RegistrationHandler.class);
    private final PlayerProfileService playerProfileService;

    public String handle(RegistrationRequest request) {
        RegistrationResponse response;
        if (playerProfileService.findByPdId(request.getAime_id()).isPresent()) {
            response = new RegistrationResponse(
                    request.getCmd(),
                    request.getReq_id(),
                    "ok",
                    Result.FAILED,
                    -1);
        } else {
            PlayerProfile profile = playerProfileService.register(request);
            response = new RegistrationResponse(
                    request.getCmd(),
                    request.getReq_id(),
                    "ok",
                    Result.SUCCESS,
                    profile.getPdId());
        }

        String resp = this.build(mapper.toMap(response));
        logger.info("Response: {}", resp);

        return resp;
    }
}
