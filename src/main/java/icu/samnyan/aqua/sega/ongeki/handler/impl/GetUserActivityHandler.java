package icu.samnyan.aqua.sega.ongeki.handler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.samnyan.aqua.sega.ongeki.dao.userdata.UserActivityRepository;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.ongeki.model.userdata.UserActivity;
import icu.samnyan.aqua.sega.util.jackson.BasicMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component("OngekiGetUserActivityHandler")
public class GetUserActivityHandler implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetUserActivityHandler.class);

    private final BasicMapper mapper;

    private final UserActivityRepository userActivityRepository;

    @Autowired
    public GetUserActivityHandler(BasicMapper mapper, UserActivityRepository userActivityRepository) {
        this.mapper = mapper;
        this.userActivityRepository = userActivityRepository;
    }


    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {
        long userId = ((Number) request.get("userId")).longValue();
        Integer kind = (Integer) request.get("kind");

        List<UserActivity> activityList = userActivityRepository.findByUser_Card_ExtIdAndKindOrderBySortNumberDesc(userId,kind);

        // Game crash workaround. Ported from minime @ Felix, commit 7efa1d7fd8d6b31cfba5f1755bf7bf3b9bc7aeb0
        if (kind == 1) {
            // PlayActivity: 15
            activityList = new ArrayList<>(activityList.subList(0, activityList.size() < 15 ? activityList.size() : 15));
        } else if (kind == 2) {
            // Music: 10
            activityList = new ArrayList<>(activityList.subList(0, activityList.size() < 10 ? activityList.size() : 10));
        }

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("userId", userId);
        resultMap.put("length", activityList.size());
        resultMap.put("kind", kind);
        resultMap.put("userActivityList", activityList);

        String json = mapper.write(resultMap);

        logger.info("Response: " + json);
        return json;
    }
}
