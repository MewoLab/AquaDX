package icu.samnyan.aqua.sega.chunithm.handler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.chunithm.model.userdata.UserMap;
import icu.samnyan.aqua.sega.chunithm.service.UserMapService;
import icu.samnyan.aqua.sega.util.jackson.StringMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Handle GetUserMap request
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
public class GetUserMapHandler implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetUserItemHandler.class);

    private final StringMapper mapper;

    private final UserMapService userMapService;

    @Autowired
    public GetUserMapHandler(StringMapper mapper, UserMapService userMapService) {
        this.mapper = mapper;
        this.userMapService = userMapService;
    }

    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {
        String userId = (String) request.get("userId");

        List<UserMap> userMapList = userMapService.getByUserId(userId);

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("userId", userId);
        resultMap.put("length", userMapList.size());
        resultMap.put("userMapList", userMapList);

        String json = mapper.write(resultMap);
        logger.info("Response: " + json);
        return json;
    }
}
