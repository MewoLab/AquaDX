package icu.samnyan.aqua.sega.chunithm.handler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.chunithm.model.response.CodeResp;
import icu.samnyan.aqua.sega.chunithm.model.userdata.UserCharge;
import icu.samnyan.aqua.sega.chunithm.model.userdata.UserData;
import icu.samnyan.aqua.sega.chunithm.service.UserChargeService;
import icu.samnyan.aqua.sega.chunithm.service.UserDataService;
import icu.samnyan.aqua.sega.util.jackson.StringMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
public class UpsertUserChargelogHandler implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(UpsertUserChargelogHandler.class);

    private final StringMapper mapper;

    private final UserDataService userDataService;
    private final UserChargeService userChargeService;

    public UpsertUserChargelogHandler(StringMapper mapper, UserDataService userDataService, UserChargeService userChargeService) {
        this.mapper = mapper;
        this.userDataService = userDataService;
        this.userChargeService = userChargeService;
    }

    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {
        String userId = (String) request.get("userId");
        UserData user = userDataService.getUserByExtId(userId).orElseThrow();

        Map<String, Object> userChargeMap = (Map<String, Object>) request.get("userCharge");
        String chargeId = (String) userChargeMap.get("chargeId");
        UserCharge charge = mapper.convert(userChargeMap, UserCharge.class);

        UserCharge userCharge = userChargeService.getByUserAndChargeId(user, Integer.parseInt(chargeId)).orElseGet(() -> new UserCharge(user));
        userCharge.setChargeId(charge.getChargeId());
        userCharge.setStock(charge.getStock());
        userCharge.setPurchaseDate(charge.getPurchaseDate());
        userCharge.setValidDate(charge.getValidDate());
        userCharge.setParam1(charge.getParam1());
        userCharge.setParam2(charge.getParam2());
        userCharge.setParamDate(charge.getParamDate());

        userChargeService.save(userCharge);

        String json = mapper.write(new CodeResp(1));
        logger.info("Response: " + json);
        return json;
    }
}
