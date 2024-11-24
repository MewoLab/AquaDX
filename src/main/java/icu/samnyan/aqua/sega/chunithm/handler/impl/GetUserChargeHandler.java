package icu.samnyan.aqua.sega.chunithm.handler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.chunithm.model.userdata.UserCharge;
import icu.samnyan.aqua.sega.chunithm.service.UserChargeService;
import icu.samnyan.aqua.sega.util.jackson.StringMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
public class GetUserChargeHandler implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetUserChargeHandler.class);

    private final StringMapper mapper;

    private final UserChargeService userChargeService;

    @Autowired
    public GetUserChargeHandler(StringMapper mapper, UserChargeService userChargeService) {
        this.mapper = mapper;
        this.userChargeService = userChargeService;
    }

    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {
        String userId = (String) request.get("userId");

        List<UserCharge> userChargeList = userChargeService.getByUserId(userId);

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("userId", userId);
        resultMap.put("length", userChargeList.size());
        resultMap.put("userChargeList", userChargeList);

        String json = mapper.write(resultMap);
        logger.info("Response: " + json);
        return json;
    }
}
