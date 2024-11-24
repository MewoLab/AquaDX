package icu.samnyan.aqua.sega.ongeki.handler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.samnyan.aqua.sega.ongeki.dao.userdata.UserEventPointRepository;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.ongeki.model.userdata.UserEventPoint;
import icu.samnyan.aqua.sega.util.jackson.BasicMapper;
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
@Component("OngekiGetUserEventPointHandler")
public class GetUserEventPointHandler implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetUserEventPointHandler.class);

    private final BasicMapper mapper;

    private final UserEventPointRepository userEventPointRepository;

    @Autowired
    public GetUserEventPointHandler(BasicMapper mapper, UserEventPointRepository userEventPointRepository) {
        this.mapper = mapper;
        this.userEventPointRepository = userEventPointRepository;
    }


    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {
        long userId = ((Number) request.get("userId")).longValue();

        List<UserEventPoint> eventPointList = userEventPointRepository.findByUser_Card_ExtId(userId);

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("userId", userId);
        resultMap.put("length", eventPointList.size());
        resultMap.put("userEventPointList", eventPointList);

        String json = mapper.write(resultMap);

        logger.info("Response: " + json);
        return json;
    }
}
