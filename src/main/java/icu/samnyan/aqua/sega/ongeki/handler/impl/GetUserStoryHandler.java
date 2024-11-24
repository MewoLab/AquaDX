package icu.samnyan.aqua.sega.ongeki.handler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.samnyan.aqua.sega.ongeki.dao.userdata.UserStoryRepository;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.ongeki.model.userdata.UserStory;
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
@Component("OngekiGetUserStoryHandler")
public class GetUserStoryHandler implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetUserStoryHandler.class);

    private final BasicMapper mapper;

    private final UserStoryRepository userStoryRepository;

    @Autowired
    public GetUserStoryHandler(BasicMapper mapper, UserStoryRepository userStoryRepository) {
        this.mapper = mapper;
        this.userStoryRepository = userStoryRepository;
    }


    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {
        long userId = ((Number) request.get("userId")).longValue();

        List<UserStory> userStoryList = userStoryRepository.findByUser_Card_ExtId(userId);

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("userId", userId);
        resultMap.put("length", userStoryList.size());
        resultMap.put("userStoryList", userStoryList);

        String json = mapper.write(resultMap);

        logger.info("Response: " + json);
        return json;
    }
}
