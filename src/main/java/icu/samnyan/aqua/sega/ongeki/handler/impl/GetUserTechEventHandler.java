package icu.samnyan.aqua.sega.ongeki.handler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;

import icu.samnyan.aqua.sega.ongeki.dao.userdata.UserTechEventRepository;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.ongeki.model.userdata.UserTechEvent;
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
@Component("OngekiGetUserTechEventHandler")
public class GetUserTechEventHandler implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetUserTechEventHandler.class);

    private final BasicMapper mapper;

    private final UserTechEventRepository userTechEventRepository;

    @Autowired
    public GetUserTechEventHandler(BasicMapper mapper, UserTechEventRepository userTechEventRepository) {
        this.mapper = mapper;
        this.userTechEventRepository = userTechEventRepository;
    }

    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {
        long userId = ((Number) request.get("userId")).longValue();
        List<UserTechEvent> techEventList = userTechEventRepository.findByUser_Card_ExtId(userId);

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("userId", userId);
        resultMap.put("length", techEventList.size());
        resultMap.put("userTechEventList", techEventList);

        String json = mapper.write(resultMap);

        logger.info("Response: " + json);
        return json;
    }
}
