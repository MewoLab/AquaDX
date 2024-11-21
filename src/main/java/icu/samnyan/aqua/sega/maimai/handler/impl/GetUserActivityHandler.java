package icu.samnyan.aqua.sega.maimai.handler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.samnyan.aqua.sega.maimai.dao.userdata.UserActivityRepository;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.maimai.model.userdata.UserActivity;
import icu.samnyan.aqua.sega.util.jackson.BasicMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component("MaimaiGetUserActivityHandler")
public class GetUserActivityHandler implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetUserActivityHandler.class);

    private final BasicMapper mapper;

    private final UserActivityRepository userActivityRepository;

    public GetUserActivityHandler(BasicMapper mapper, UserActivityRepository userActivityRepository) {
        this.mapper = mapper;
        this.userActivityRepository = userActivityRepository;
    }

    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {
        long userId = ((Number) request.get("userId")).longValue();
        Integer kind = (Integer) request.get("kind");

        List<UserActivity> userActivityList = userActivityRepository.findByUser_Card_ExtIdAndKind(userId, kind);

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("userId", userId);
        resultMap.put("length", userActivityList.size());
        resultMap.put("kind", kind);
        resultMap.put("gameRankingList", userActivityList);

        String json = mapper.write(resultMap);
        logger.info("Response: " + json);
        return json;
    }
}
