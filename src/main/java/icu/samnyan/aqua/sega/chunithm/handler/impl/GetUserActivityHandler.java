package icu.samnyan.aqua.sega.chunithm.handler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.chunithm.model.userdata.UserActivity;
import icu.samnyan.aqua.sega.chunithm.service.UserActivityService;
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
public class GetUserActivityHandler implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetUserActivityHandler.class);

    private final StringMapper mapper;

    private final UserActivityService userActivityService;

    @Autowired
    public GetUserActivityHandler(StringMapper mapper, UserActivityService userActivityService) {
        this.mapper = mapper;
        this.userActivityService = userActivityService;
    }


    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {
        String userId = (String) request.get("userId");
        String kind = (String) request.get("kind");

        List<UserActivity> userActivityList = userActivityService.getAllByUserIdAndKind(userId, kind);

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("userId", userId);
        resultMap.put("length", userActivityList.size());
        resultMap.put("kind", kind);
        resultMap.put("userActivityList", userActivityList);

        String json = mapper.write(resultMap);
        logger.info("Response: " + json);
        return json;
    }
}
