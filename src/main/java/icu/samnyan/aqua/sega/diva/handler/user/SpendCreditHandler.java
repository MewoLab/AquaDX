package icu.samnyan.aqua.sega.diva.handler.user;

import icu.samnyan.aqua.sega.diva.exception.ProfileNotFoundException;
import icu.samnyan.aqua.sega.diva.handler.BaseHandler;
import icu.samnyan.aqua.sega.diva.model.request.user.SpendCreditRequest;
import icu.samnyan.aqua.sega.diva.model.response.user.SpendCreditResponse;
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
public class SpendCreditHandler extends BaseHandler {
    private static final Logger logger = LoggerFactory.getLogger(SpendCreditHandler.class);
    private final PlayerProfileService playerProfileService;

    public String handle(SpendCreditRequest request) {

        PlayerProfile profile = playerProfileService.findByPdId(request.getPd_id()).orElseThrow(ProfileNotFoundException::new);

        SpendCreditResponse response = new SpendCreditResponse(
                request.getCmd(),
                request.getReq_id(),
                "ok",
                "-1,-1,x,-1,-1,x,x,-1,x,-1,-1,x,-1,-1,x,x,-1,x,-1,-1,x,-1,-1,x,x,-1,x,-1,-1,x,-1,-1,x,x,-1,x,-1,-1,x,-1,-1,x,x,-1,x,-1,-1,x,-1,-1,x,x,-1,x",
                0,
                profile.getVocaloidPoints(),
                profile.getLevelTitle(),
                profile.getPlateEffectId(),
                profile.getPlateId()
        );

        String resp = this.build(mapper.toMap(response));
        logger.info("Response: {}", resp);

        return resp;
    }
}
