package icu.samnyan.aqua.sega.ongeki.handler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.samnyan.aqua.sega.ongeki.dao.userdata.UserEventPointRepository;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.ongeki.model.response.data.UserEventRankingItem;
import icu.samnyan.aqua.sega.ongeki.model.userdata.UserEventPoint;
import icu.samnyan.aqua.sega.util.jackson.BasicMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component("OngekiGetUserEventRankingHandler")
public class GetUserEventRankingHandler implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetUserEventRankingHandler.class);

    private final BasicMapper mapper;

    private final UserEventPointRepository userEventPointRepository;

    @Autowired
    public GetUserEventRankingHandler(BasicMapper mapper, UserEventPointRepository userEventPointRepository) {
        this.mapper = mapper;
        this.userEventPointRepository = userEventPointRepository;
    }


    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {
        long userId = ((Number) request.get("userId")).longValue();
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.0"));

        List<UserEventPoint> eventPointList = userEventPointRepository.findByUser_Card_ExtId(userId);
        List<UserEventRankingItem> rankingItemList = new LinkedList<>();
        eventPointList.forEach(x -> rankingItemList.add(new UserEventRankingItem(
                x.getEventId(),
                1, // Type 1 is latest ranking
                time,
                userEventPointRepository.calculateRankByUserAndEventId(x.getUser().getId(), x.getEventId()),
                x.getPoint()
        )));

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("userId", userId);
        resultMap.put("length", rankingItemList.size());
        resultMap.put("userEventRankingList", rankingItemList);

        String json = mapper.write(resultMap);

        logger.info("Response: " + json);
        return json;
    }
}
