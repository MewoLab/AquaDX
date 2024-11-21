package icu.samnyan.aqua.sega.ongeki.handler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.samnyan.aqua.sega.ongeki.dao.userdata.UserTrainingRoomRepository;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.ongeki.model.userdata.UserTrainingRoom;
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
@Component("OngekiGetUserTrainingRoomByKeyHandler")
public class GetUserTrainingRoomByKeyHandler implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetUserTrainingRoomByKeyHandler.class);

    private final BasicMapper mapper;

    private final UserTrainingRoomRepository userTrainingRoomRepository;

    @Autowired
    public GetUserTrainingRoomByKeyHandler(BasicMapper mapper, UserTrainingRoomRepository userTrainingRoomRepository) {
        this.mapper = mapper;
        this.userTrainingRoomRepository = userTrainingRoomRepository;
    }


    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {
        long userId = ((Number) request.get("userId")).longValue();

        List<UserTrainingRoom> trainingRoomList = userTrainingRoomRepository.findByUser_Card_ExtId(userId);

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("userId", userId);
        resultMap.put("length", trainingRoomList.size());
        resultMap.put("userTrainingRoomList", trainingRoomList);

        String json = mapper.write(resultMap);

        logger.info("Response: " + json);
        return json;
    }
}
