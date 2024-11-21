package icu.samnyan.aqua.sega.maimai.handler.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import icu.samnyan.aqua.sega.maimai.dao.userdata.UserSurvivalRepository;
import icu.samnyan.aqua.sega.general.BaseHandler;
import icu.samnyan.aqua.sega.maimai.model.userdata.UserSurvival;
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
@Component("MaimaiGetUserSurvivalHandler")
public class GetUserSurvivalHandler implements BaseHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetUserSurvivalHandler.class);

    private final BasicMapper mapper;

    private final UserSurvivalRepository userSurvivalRepository;

    public GetUserSurvivalHandler(BasicMapper mapper, UserSurvivalRepository userSurvivalRepository) {
        this.mapper = mapper;
        this.userSurvivalRepository = userSurvivalRepository;
    }

    @Override
    public String handle(Map<String, ?> request) throws JsonProcessingException {
        long userId = ((Number) request.get("userId")).longValue();

        List<UserSurvival> userSurvivalList = userSurvivalRepository.findByUser_Card_ExtId(userId);

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("userId", userId);
        resultMap.put("length", userSurvivalList.size());
        resultMap.put("userSurvivalList", userSurvivalList);

        String json = mapper.write(resultMap);
        logger.info("Response: " + json);
        return json;
    }
}
