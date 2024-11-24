package icu.samnyan.aqua.sega.ongeki.handler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.samnyan.aqua.sega.ongeki.dao.userdata.UserMissionPointRepository;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.ongeki.model.userdata.UserMissionPoint;
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
@Component("OngekiGetUserMissionPointHandler")
public class GetUserMissionPointHandler implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetUserMissionPointHandler.class);

    private final BasicMapper mapper;

    private final UserMissionPointRepository userMissionPointRepository;

    @Autowired
    public GetUserMissionPointHandler(BasicMapper mapper, UserMissionPointRepository userMissionPointRepository) {
        this.mapper = mapper;
        this.userMissionPointRepository = userMissionPointRepository;
    }


    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {
        long userId = ((Number) request.get("userId")).longValue();

        List<UserMissionPoint> missionPointList = userMissionPointRepository.findByUser_Card_ExtId(userId);

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("userId", userId);
        resultMap.put("length", missionPointList.size());
        resultMap.put("userMissionPointList", missionPointList);

        String json = mapper.write(resultMap);

        logger.info("Response: " + json);
        return json;
    }
}
