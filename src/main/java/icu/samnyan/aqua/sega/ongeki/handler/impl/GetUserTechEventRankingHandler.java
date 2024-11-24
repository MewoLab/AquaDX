package icu.samnyan.aqua.sega.ongeki.handler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;

import icu.samnyan.aqua.sega.ongeki.dao.userdata.UserTechEventRepository;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.ongeki.model.response.data.UserTechEventRankingItem;
import icu.samnyan.aqua.sega.ongeki.model.userdata.UserTechEvent;
import icu.samnyan.aqua.sega.util.jackson.BasicMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component("OngekiGetUserTechEventRankingHandler")
public class GetUserTechEventRankingHandler implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetUserTechEventRankingHandler.class);

    private final BasicMapper mapper;

    private final UserTechEventRepository userTechEventRepository;

    @Autowired
    public GetUserTechEventRankingHandler(BasicMapper mapper, UserTechEventRepository userTechEventRepository) {
        this.mapper = mapper;
        this.userTechEventRepository = userTechEventRepository;
    }

    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {
        long userId = ((Number) request.get("userId")).longValue();

        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.0"));

        List<UserTechEvent> techEventList = userTechEventRepository.findByUser_Card_ExtId(userId);
        List<UserTechEventRankingItem> techEventRankingList = new ArrayList<>();
        techEventList.forEach(x -> techEventRankingList.add(new UserTechEventRankingItem(
            x.getEventId(),
            time,
            1,
            x.getTotalTechScore(),
            x.getTotalPlatinumScore()
            )));

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("userId", userId);
        resultMap.put("length", techEventRankingList.size());
        resultMap.put("userTechEventRankingList", techEventRankingList);

        String json = mapper.write(resultMap);

        logger.info("Response: " + json);
        return json;
    }
}
